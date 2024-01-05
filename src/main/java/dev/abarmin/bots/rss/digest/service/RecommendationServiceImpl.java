package dev.abarmin.bots.rss.digest.service;

import dev.abarmin.bots.rss.reader.persistence.ArticleSource;
import dev.abarmin.bots.rss.reader.persistence.ArticleSourceRepository;
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
