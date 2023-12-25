package dev.abarmin.bots.rss.reader.service;

import dev.abarmin.bots.rss.reader.persistence.Article;

public interface ArticleService {
    /**
     * Check if article already known to the system.
     *
     * @param article to check.
     * @return true if it is an existing article and false otherwise.
     */
    boolean isAvailable(Article article);

    /**
     * Save a provided article.
     *
     * @param article to save.
     * @return saved article.
     */
    Article save(Article article);
}
