package dev.abarmin.bots.service;

import dev.abarmin.bots.entity.Article;
import dev.abarmin.bots.entity.ArticleSource;

import java.util.Collection;

public interface ArticleReaderService {
    /**
     * Read articles from given source. Return all the articles including
     * already read.
     *
     * @param source of articles.
     * @return a collection of all the articles from given source.
     */
    Collection<Article> read(ArticleSource source);
}
