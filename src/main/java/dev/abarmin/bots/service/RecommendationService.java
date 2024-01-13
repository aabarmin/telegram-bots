package dev.abarmin.bots.service;

import dev.abarmin.bots.entity.rss.ArticleSource;

import java.util.Collection;

public interface RecommendationService {
    Collection<ArticleSource> findRecommendations();
}
