package dev.abarmin.bots.controller;

import dev.abarmin.bots.controller.model.ArticleEpisode;
import dev.abarmin.bots.controller.model.ArticleRow;
import dev.abarmin.bots.entity.episodes.EpisodeArticle;
import dev.abarmin.bots.repository.ArticleSourceRepository;
import dev.abarmin.bots.repository.EpisodeArticlesRepository;
import dev.abarmin.bots.repository.EpisodesRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

import static dev.abarmin.bots.entity.jooq.Tables.ARTICLES;
import static dev.abarmin.bots.entity.jooq.Tables.ARTICLE_SOURCES;
import static dev.abarmin.bots.entity.jooq.Tables.EPISODES;
import static dev.abarmin.bots.entity.jooq.Tables.EPISODES_ARTICLES;

@Controller
@RequiredArgsConstructor
public class ArticlesController {
    private final DSLContext dslContext;
    private final EpisodesRepository episodesRepository;
    private final EpisodeArticlesRepository episodeArticlesRepository;
    private final ArticleSourceRepository articleSourceRepository;

    @GetMapping("/articles")
    public ModelAndView index(ModelAndView modelAndView,
                              @RequestParam(value = "show_all", defaultValue = "false") boolean showAll,
                              @RequestParam(value = "sources", defaultValue = "") String sourcesString) {
        modelAndView.addObject("episodes", episodesRepository.findAll());
        modelAndView.addObject("articlesList", ArticlesList.builder()
                .selectedSources(getSources(sourcesString))
                .showAll(showAll)
                .build());
        modelAndView.addObject("articles", getArticles(showAll, getSources(sourcesString)));
        modelAndView.addObject("sources", articleSourceRepository.findAll());
        modelAndView.setViewName("articles/index");
        return modelAndView;
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

    private Collection<ArticleRow> getArticles(boolean showAll, Collection<Integer> sources) {
        var builder = dslContext.select(
                        ARTICLES.ARTICLE_ID,
                        ARTICLES.ARTICLE_TITLE,
                        ARTICLES.ARTICLE_URL,
                        ARTICLES.ARTICLE_ADDED,
                        ARTICLE_SOURCES.SOURCE_ID,
                        ARTICLE_SOURCES.SOURCE_NAME)
                .from(ARTICLES)
                .innerJoin(ARTICLE_SOURCES).on(ARTICLES.ARTICLE_SOURCE_ID.eq(ARTICLE_SOURCES.SOURCE_ID));
        var conditions = DSL.trueCondition();

        if (!showAll) {
            builder = builder.leftJoin(EPISODES_ARTICLES).on(EPISODES_ARTICLES.ARTICLE_ID.eq(ARTICLES.ARTICLE_ID));
            conditions.add(EPISODES_ARTICLES.EPISODE_ID.isNull());
        }
        if (!sources.isEmpty()) {
            conditions.add(ARTICLE_SOURCES.SOURCE_ID.in(sources));
        }
        return builder.where(conditions)
                .orderBy(ARTICLES.ARTICLE_ADDED.desc())
                .fetch()
                .map(record -> {
                    return ArticleRow.builder()
                            .articleId(record.get(ARTICLES.ARTICLE_ID))
                            .articleTitle(record.get(ARTICLES.ARTICLE_TITLE))
                            .articleUrl(record.get(ARTICLES.ARTICLE_URL))
                            .articleAdded(record.get(ARTICLES.ARTICLE_ADDED))
                            .sourceId(record.get(ARTICLE_SOURCES.SOURCE_ID))
                            .sourceName(record.get(ARTICLE_SOURCES.SOURCE_NAME))
                            .episodes(getEpisodes(record.get(ARTICLES.ARTICLE_ID)))
                            .build();
                });
    }

    private Collection<ArticleEpisode> getEpisodes(int articleId) {
        return dslContext.select(EPISODES.EPISODE_ID, EPISODES.EPISODE_NAME)
                .from(EPISODES)
                .innerJoin(EPISODES_ARTICLES).on(EPISODES_ARTICLES.EPISODE_ID.eq(EPISODES.EPISODE_ID))
                .innerJoin(ARTICLES).on(EPISODES_ARTICLES.ARTICLE_ID.eq(ARTICLES.ARTICLE_ID))
                .where(ARTICLES.ARTICLE_ID.eq(articleId))
                .fetch()
                .map(record -> {
                    return ArticleEpisode.builder()
                            .episodeId(record.get(EPISODES.EPISODE_ID))
                            .episodeName(record.get(EPISODES.EPISODE_NAME))
                            .build();
                });
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
    }
}
