package dev.abarmin.bots.repository;

import dev.abarmin.bots.entity.episodes.Episode;
import org.springframework.data.repository.CrudRepository;

public interface EpisodesRepository extends CrudRepository<Episode, Integer> {
}
