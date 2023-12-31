package dev.abarmin.bots.rss.reader.persistence;

import org.springframework.data.repository.ListCrudRepository;

import java.net.URI;
import java.util.Optional;

public interface ArticleSourceRepository extends ListCrudRepository<ArticleSource, Integer> {
    /**
     * Find one by source URI.
     *
     * @param url
     * @return
     */
    Optional<ArticleSource> findBySourceUri(URI url);
}
