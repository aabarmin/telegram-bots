package dev.abarmin.bots.service.impl;

import dev.abarmin.bots.entity.rss.ArticleSource;
import dev.abarmin.bots.repository.ArticleSourceRepository;
import dev.abarmin.bots.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final ArticleSourceRepository repository;

    @Override
    public Collection<ArticleSource> findRecommendations() {
        return repository.findPopularSources()
                .stream()
                .limit(5)
                .collect(Collectors.toList());
    }
}
