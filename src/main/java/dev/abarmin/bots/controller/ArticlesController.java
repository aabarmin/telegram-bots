package dev.abarmin.bots.controller;

import dev.abarmin.bots.controller.model.ArticleEpisode;
import dev.abarmin.bots.controller.model.ArticleRow;
import dev.abarmin.bots.entity.episodes.EpisodeArticle;
import dev.abarmin.bots.repository.EpisodeArticlesRepository;
import dev.abarmin.bots.repository.EpisodesRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class ArticlesController {
    private final JdbcClient jdbcClient;
    private final EpisodesRepository episodesRepository;
    private final EpisodeArticlesRepository episodeArticlesRepository;

    @GetMapping("/articles")
    public ModelAndView index(ModelAndView modelAndView,
                              @RequestParam(value = "not_in_episodes", defaultValue = "false") boolean notInEpisodes) {
        modelAndView.addObject("episodes", episodesRepository.findAll());
        modelAndView.addObject("selected", new ArticlesList());
        modelAndView.addObject("articles", getArticles(notInEpisodes));
        modelAndView.addObject("notInEpisodes", notInEpisodes);
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

        return new RedirectView("/articles");
    }

    private boolean notYetAdded(EpisodeArticle ea) {
        return episodeArticlesRepository.findByEpisodeIdAndArticleId(
                ea.getEpisodeId().getId(),
                ea.getArticleId().getId()).isEmpty();
    }

    private Collection<ArticleRow> getArticles(boolean notInEpisodes) {
        String query = """
                select
                    article.article_id,
                    article.article_title,
                    source.source_id,
                    source.source_name
                from articles article
                inner join article_sources source on article.article_source_id = source.source_id
                """;

        if (notInEpisodes) {
            query += """
                left join episodes_articles ea on ea.article_id = article.article_id
                where ea.episode_id is null
            """;
        }

        return jdbcClient.sql(query)
                .query((rs, rowNum) -> ArticleRow.builder()
                        .articleId(rs.getInt("article_id"))
                        .articleTitle(rs.getString("article_title"))
                        .sourceId(rs.getInt("source_id"))
                        .sourceName(rs.getString("source_name"))
                        .episodes(getEpisodes(rs.getInt("article_id")))
                        .build())
                .list();
    }

    private Collection<ArticleEpisode> getEpisodes(int articleId) {
        final String query = """
                select
                    episode.episode_id,
                    episode.episode_name
                from episodes episode
                inner join episodes_articles ea on ea.episode_id = episode.episode_id
                inner join articles article on article.article_id = ea.article_id
                where article.article_id = ?
                """;

        return jdbcClient.sql(query)
                .param(articleId)
                .query((rs, rowNum) -> ArticleEpisode.builder()
                        .episodeId(rs.getString("episode_id"))
                        .episodeName(rs.getString("episode_name"))
                        .build())
                .list();
    }

    @Data
    public static class ArticlesList {
        private Collection<Integer> selected;
        private int episodeId;
    }
}
