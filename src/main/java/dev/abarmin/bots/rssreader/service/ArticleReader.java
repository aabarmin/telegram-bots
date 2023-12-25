package dev.abarmin.bots.rssreader.service;

import dev.abarmin.bots.rssreader.persistence.Article;
import dev.abarmin.bots.rssreader.persistence.ArticleSource;

import java.util.Collection;

public interface ArticleReader {
    /**
     * Read articles from given source. Return all the articles including
     * already read.
     *
     * @param source of articles.
     * @return a collection of all the articles from given source.
     */
    Collection<Article> read(ArticleSource source);
}
