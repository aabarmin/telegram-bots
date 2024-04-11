package dev.abarmin.bots.controller;

import dev.abarmin.bots.controller.model.ArticleRow;
import dev.abarmin.bots.entity.episodes.Episode;
import dev.abarmin.bots.repository.EpisodeArticlesRepository;
import dev.abarmin.bots.repository.EpisodesRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collection;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class EpisodesController {
    private final EpisodeArticlesRepository episodeArticlesRepository;
    private final EpisodesRepository repository;
    private final JdbcClient jdbcClient;

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
        modelAndView.addObject("selected", new ArticleList());
        modelAndView.setViewName("episodes/edit");
        return modelAndView;
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
        final String query = """
                select
                    a.article_id,
                    a.article_title,
                    source.source_id,
                    source.source_name
                from articles a
                inner join article_sources source on a.article_source_id = source.source_id
                inner join episodes_articles ea on ea.article_id = a.article_id
                where ea.episode_id = ?
                """;
        return jdbcClient.sql(query)
                .param(episodeId)
                .query((rs, no) -> ArticleRow.builder()
                        .articleId(rs.getInt("article_id"))
                        .articleTitle(rs.getString("article_title"))
                        .sourceId(rs.getInt("source_id"))
                        .sourceName(rs.getString("source_name"))
                        .build())
                .list();
    }
}
