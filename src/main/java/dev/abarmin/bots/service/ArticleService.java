package dev.abarmin.bots.service;

import dev.abarmin.bots.entity.rss.Article;

public interface ArticleService {
    /**
     * Save a provided article.
     *
     * @param article to save.
     * @return saved article.
     */
    Article save(Article article);
}
