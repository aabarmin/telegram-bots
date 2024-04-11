package dev.abarmin.bots.controller;

import dev.abarmin.bots.controller.model.ArticleEpisode;
import dev.abarmin.bots.controller.model.ArticleRow;
import dev.abarmin.bots.entity.episodes.EpisodeArticle;
import dev.abarmin.bots.repository.ArticleSourceRepository;
import dev.abarmin.bots.repository.EpisodeArticlesRepository;
import dev.abarmin.bots.repository.EpisodesRepository;
import dev.abarmin.bots.scheduler.JdbcHelper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ArticlesController {
    private final JdbcClient jdbcClient;
    private final EpisodesRepository episodesRepository;
    private final EpisodeArticlesRepository episodeArticlesRepository;
    private final ArticleSourceRepository articleSourceRepository;

    @GetMapping("/articles")
    public ModelAndView index(ModelAndView modelAndView,
                              @RequestParam(value = "show_all", defaultValue = "false") boolean showAll,
                              @RequestParam(value = "sources", defaultValue = "") String sourcesString) {
        modelAndView.addObject("episodes", episodesRepository.findAll());
        modelAndView.addObject("selected", new ArticlesList());
        modelAndView.addObject("articles", getArticles(showAll, getSources(sourcesString)));
        modelAndView.addObject("sources", articleSourceRepository.findAll());
        modelAndView.addObject("selectedSources", getSources(sourcesString));
        modelAndView.addObject("showAll", showAll);
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

    private Collection<Integer> getSources(String sourcesString) {
        if (StringUtils.isEmpty(sourcesString)) {
            return List.of();
        }
        return Arrays.stream(sourcesString.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    private Collection<ArticleRow> getArticles(boolean showAll, Collection<Integer> sources) {
        String query = """
                select
                    article.article_id,
                    article.article_title,
                    article.article_url,
                    article.article_added,
                    source.source_id,
                    source.source_name
                from articles article
                inner join article_sources source on article.article_source_id = source.source_id
                """;

        if (!showAll) {
            query += """
                        left join episodes_articles ea on ea.article_id = article.article_id
                        where ea.episode_id is null
                    """;

        }
        if (!sources.isEmpty()) {
            if (showAll) {
                query += "where ";
            } else {
                query += "and ";
            }
            query += " source.source_id in (:sources) ";
        }
        query += """
                order by article.article_added desc
                """;

        JdbcClient.StatementSpec sqlSpec = jdbcClient.sql(query);
        if (!sources.isEmpty()) {
            sqlSpec.param("sources", sources);
        }
        return sqlSpec
                .query((rs, rowNum) -> ArticleRow.builder()
                        .articleId(rs.getInt("article_id"))
                        .articleTitle(rs.getString("article_title"))
                        .articleUrl(rs.getString("article_url"))
                        .articleAdded(JdbcHelper.readLocalDateTime(rs.getTimestamp("article_added")))
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
