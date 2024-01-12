package dev.abarmin.bots.service;

import dev.abarmin.bots.rss.reader.persistence.ArticleSource;

import java.util.Collection;

public interface RecommendationService {
    Collection<ArticleSource> findRecommendations();
}
