package dev.abarmin.bots.repository;

import dev.abarmin.bots.entity.episodes.Episode;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface EpisodesRepository extends
        CrudRepository<Episode, Integer>,
        PagingAndSortingRepository<Episode, Integer> {

    @Override
    default Iterable<Episode> findAll() {
        return findAll(Sort.by(Sort.Direction.ASC, "episodeName"));
    }

    Optional<Episode> findByEpisodeName(String name);
}
