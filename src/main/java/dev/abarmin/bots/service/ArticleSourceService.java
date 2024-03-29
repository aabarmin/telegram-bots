package dev.abarmin.bots.service;

import dev.abarmin.bots.entity.rss.ArticleSource;

import java.net.URI;
import java.util.Collection;

public interface ArticleSourceService {
    /**
     * Get all article sources.
     *
     * @return
     */
    Collection<ArticleSource> findAllSources();

    /**
     * Find or create an RSS subscription.
     *
     * @param rssUri
     * @return
     */
    ArticleSource findOrCreate(URI rssUri);
}
