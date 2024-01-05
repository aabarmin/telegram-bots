package dev.abarmin.bots.rss.digest.service;

import dev.abarmin.bots.rss.reader.persistence.ArticleSource;

import java.util.Collection;

public interface RecommendationService {
    Collection<ArticleSource> findRecommendations();
}
