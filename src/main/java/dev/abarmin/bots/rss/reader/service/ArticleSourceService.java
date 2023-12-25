package dev.abarmin.bots.rss.reader.service;

import dev.abarmin.bots.rss.reader.persistence.ArticleSource;

import java.util.Collection;

public interface ArticleSourceService {
    /**
     * Get all article sources.
     *
     * @return
     */
    Collection<ArticleSource> findAllSources();
}
