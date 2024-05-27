package dev.abarmin.bots.repository;

import dev.abarmin.bots.entity.episodes.Episode;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface EpisodesRepository extends
        ListCrudRepository<Episode, Integer>,
        PagingAndSortingRepository<Episode, Integer> {

    @Override
    default List<Episode> findAll() {
        return findAll(Sort.by(Sort.Direction.ASC, "episodeName"));
    }

    List<Episode> findAll(Sort sort);

    Optional<Episode> findByEpisodeName(String name);
}
