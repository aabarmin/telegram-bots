package dev.abarmin.bots.controller;

import dev.abarmin.bots.entity.episodes.Episode;
import dev.abarmin.bots.entity.episodes.EpisodeArticle;
import dev.abarmin.bots.entity.episodes.EpisodeStatus;
import dev.abarmin.bots.entity.rss.ArticleSource;
import dev.abarmin.bots.model.ArticlesRequest;
import dev.abarmin.bots.model.ArticlesResponse;
import dev.abarmin.bots.repository.ArticleSourceRepository;
import dev.abarmin.bots.repository.EpisodeArticlesRepository;
import dev.abarmin.bots.repository.EpisodesRepository;
import dev.abarmin.bots.service.ArticleProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.abarmin.bots.service.DefaultEpisodeCreator.DEFAULT_EPISODE_NAME;

@Controller
@RequiredArgsConstructor
public class ArticlesController {
    private final EpisodesRepository episodesRepository;
    private final EpisodeArticlesRepository episodeArticlesRepository;
    private final ArticleSourceRepository articleSourceRepository;
    private final ArticleProvider articleProvider;

    @ModelAttribute("episodes")
    public Collection<Episode> episodes() {
        return episodesRepository.findAll()
                .stream()
                .filter(ep -> ep.getEpisodeStatus() != EpisodeStatus.PUBLISHED)
                .toList();
    }

    @ModelAttribute("sources")
    public Iterable<ArticleSource> sources() {
        return articleSourceRepository.findAll();
    }

    @GetMapping("/articles")
    public ModelAndView index(ModelAndView modelAndView,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "show_all", defaultValue = "false") boolean showAll,
                              @RequestParam(value = "sources", defaultValue = "") String sourcesString) {

        ArticlesResponse articles = articleProvider.getArticles(ArticlesRequest.builder()
                .page(page)
                .showAll(showAll)
                .sources(getSources(sourcesString))
                .build());

        modelAndView.addObject("articlesList", ArticlesList.builder()
                .selectedSources(getSources(sourcesString))
                .showAll(showAll)
                .episodeId(getDefaultEpisodeId())
                .page(page)
                .build());
        modelAndView.addObject("articles", articles.getArticles());
        modelAndView.addObject("page", articles.getPage());
        modelAndView.addObject("totalPages", articles.getTotalPages());
        modelAndView.setViewName("articles/index");
        return modelAndView;
    }

    private int getDefaultEpisodeId() {
        return episodesRepository.findByEpisodeName(DEFAULT_EPISODE_NAME)
                .map(Episode::getId)
                .orElseThrow(() -> new RuntimeException("No default episode"));
    }

    @PostMapping(value = "/articles", params = {"action=add"})
    public RedirectView addToEpisode(ArticlesList articlesList) {
        Collection<EpisodeArticle> newMapping = articlesList.getSelected().stream()
                .map(articleId -> EpisodeArticle.builder()
                        .articleId(AggregateReference.to(articleId))
                        .episodeId(AggregateReference.to(articlesList.getEpisodeId()))
                        .build())
                .filter(this::notYetAdded)
                .toList();
        episodeArticlesRepository.saveAll(newMapping);

        Map<String, String> params = new HashMap<>();
        if (articlesList.isShowAll()) {
            params.put("show_all", "true");
        }
        if (!articlesList.getSelectedSources().isEmpty()) {
            params.put("sources", StringUtils.join(articlesList.getSelectedSources(), ","));
        }
        String redirectUrl = "/articles?" + params.entrySet()
                .stream()
                .map(kv -> kv.getKey() + "=" + kv.getValue())
                .collect(Collectors.joining("&"));

        return new RedirectView(redirectUrl);
    }

    private boolean notYetAdded(EpisodeArticle ea) {
        return episodeArticlesRepository.findByEpisodeIdAndArticleId(
                ea.getEpisodeId().getId(),
                ea.getArticleId().getId()).isEmpty();
    }

    private Collection<Integer> getSources(String sourcesString) {
        if (StringUtils.isEmpty(sourcesString)) {
            return List.of();
        }
        return Arrays.stream(sourcesString.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticlesList {
        private Collection<Integer> selected;
        private Collection<Integer> selectedSources;
        private boolean showAll;
        private int episodeId;
        private int page;
    }
}
