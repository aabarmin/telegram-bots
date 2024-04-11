package dev.abarmin.bots.repository;

import dev.abarmin.bots.entity.episodes.EpisodeArticle;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EpisodeArticlesRepository extends CrudRepository<EpisodeArticle, Integer> {
    Optional<EpisodeArticle> findByEpisodeIdAndArticleId(Integer episodeId, Integer articleId);
}
