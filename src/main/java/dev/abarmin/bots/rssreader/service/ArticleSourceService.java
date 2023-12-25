package dev.abarmin.bots.rssreader.service;

import dev.abarmin.bots.rssreader.persistence.ArticleSource;

import java.util.Collection;

public interface ArticleSourceService {
    /**
     * Get all article sources.
     *
     * @return
     */
    Collection<ArticleSource> findAllSources();
}
