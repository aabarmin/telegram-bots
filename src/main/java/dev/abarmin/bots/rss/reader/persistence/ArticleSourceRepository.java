package dev.abarmin.bots.rss.reader.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface ArticleSourceRepository extends CrudRepository<ArticleSource, Integer> {
    /**
     * Check if an article source with given name already exists.
     *
     * @param name
     * @return
     */
    boolean existsBySourceName(String name);

    /**
     * Return all the sources as a collection.
     * @return
     */
    default Collection<ArticleSource> findAll() {
        return findAllByOrderBySourceNameAsc();
    }

    /**
     * Return all the sources sorted.
     * @return
     */
    Collection<ArticleSource> findAllByOrderBySourceNameAsc();
}
