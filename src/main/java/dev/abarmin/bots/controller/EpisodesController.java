package dev.abarmin.bots.controller;

import dev.abarmin.bots.controller.model.ArticleRow;
import dev.abarmin.bots.entity.episodes.Episode;
import dev.abarmin.bots.repository.EpisodeArticlesRepository;
import dev.abarmin.bots.repository.EpisodesRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static dev.abarmin.bots.entity.jooq.Tables.ARTICLES;
import static dev.abarmin.bots.entity.jooq.Tables.ARTICLE_SOURCES;
import static dev.abarmin.bots.entity.jooq.Tables.EPISODES_ARTICLES;

@Controller
@RequiredArgsConstructor
public class EpisodesController {
    private final EpisodeArticlesRepository episodeArticlesRepository;
    private final EpisodesRepository repository;
    private final DSLContext dslContext;

    @GetMapping("/episodes")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.addObject("episodes", repository.findAll());
        modelAndView.setViewName("episodes/index");
        return modelAndView;
    }

    @GetMapping("/episodes/{id}")
    public ModelAndView edit(ModelAndView modelAndView, @PathVariable("id") int episodeId) {
        modelAndView.addObject("episode", repository.findById(episodeId).orElseThrow());
        modelAndView.addObject("articles", getArticles(episodeId));
        modelAndView.addObject("articlesDigest", buildDigest(getArticles(episodeId)));
        modelAndView.addObject("selected", new ArticleList());
        modelAndView.setViewName("episodes/edit");
        return modelAndView;
    }

    private String buildDigest(Collection<ArticleRow> articles) {
        return articles.stream()
                .map(art -> String.format("* %s - %s", art.getArticleTitle(), art.getArticleUrl()))
                .collect(Collectors.joining("\n"));
    }

    @GetMapping("/episodes/add")
    public ModelAndView add(ModelAndView modelAndView) {
        modelAndView.addObject("episode", new Episode());
        modelAndView.setViewName("episodes/edit");
        return modelAndView;
    }

    @PostMapping(value = "/episodes/{id}", params = {"action=remove"})
    public RedirectView removeArticles(@PathVariable("id") int episodeId,
                                       ArticleList articleList) {

        articleList.getSelected()
                .stream()
                .map(articleId -> episodeArticlesRepository.findByEpisodeIdAndArticleId(
                        episodeId, articleId
                ))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(episodeArticlesRepository::delete);

        return new RedirectView("/episodes/" + episodeId);
    }

    @PostMapping("/episodes")
    public RedirectView save(Episode episode) {
        Episode saved = repository.save(episode);
        return new RedirectView("/episodes/" + saved.getId());
    }

    @Data
    public static class ArticleList {
        private Collection<Integer> selected;
    }

    private Collection<ArticleRow> getArticles(int episodeId) {
        return dslContext.select(
                ARTICLES.ARTICLE_ID,
                ARTICLES.ARTICLE_TITLE,
                ARTICLES.ARTICLE_URL,
                ARTICLE_SOURCES.SOURCE_ID,
                ARTICLE_SOURCES.SOURCE_NAME)
                .from(ARTICLES)
                .innerJoin(ARTICLE_SOURCES).on(ARTICLES.ARTICLE_SOURCE_ID.eq(ARTICLE_SOURCES.SOURCE_ID))
                .innerJoin(EPISODES_ARTICLES).on(EPISODES_ARTICLES.ARTICLE_ID.eq(ARTICLES.ARTICLE_ID))
                .where(EPISODES_ARTICLES.EPISODE_ID.eq(episodeId))
                .fetch(record -> {
                    return ArticleRow.builder()
                            .articleId(record.get(ARTICLES.ARTICLE_ID))
                            .articleTitle(record.get(ARTICLES.ARTICLE_TITLE))
                            .articleUrl(record.get(ARTICLES.ARTICLE_URL))
                            .sourceId(record.get(ARTICLE_SOURCES.SOURCE_ID))
                            .sourceName(record.get(ARTICLE_SOURCES.SOURCE_NAME))
                            .build();
                });
    }
}
