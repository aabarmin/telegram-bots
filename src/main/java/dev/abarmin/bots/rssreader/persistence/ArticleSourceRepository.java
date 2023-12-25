package dev.abarmin.bots.rssreader.persistence;

import org.springframework.data.repository.CrudRepository;

public interface ArticleSourceRepository extends CrudRepository<ArticleSource, Integer> {
    /**
     * Check if an article source with given name already exists.
     *
     * @param name
     * @return
     */
    boolean existsBySourceName(String name);
}
