package dev.abarmin.bots.rss.reader.service;

import dev.abarmin.bots.rss.reader.persistence.Article;

public interface ArticleService {
    /**
     * Save a provided article.
     *
     * @param article to save.
     * @return saved article.
     */
    Article save(Article article);
}
