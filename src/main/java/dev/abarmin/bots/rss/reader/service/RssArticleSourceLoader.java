package dev.abarmin.bots.rss.reader.service;

import dev.abarmin.bots.rss.reader.config.RssReaderProperties;
import dev.abarmin.bots.rss.reader.persistence.ArticleSource;
import dev.abarmin.bots.rss.reader.persistence.ArticleSourceRepository;
import dev.abarmin.bots.rss.reader.config.RssReaderSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class RssArticleSourceLoader implements ApplicationRunner {
    private final ArticleSourceRepository repository;
    private final RssReaderProperties properties;

    @Override
    public void run(ApplicationArguments args) {
        properties.sources()
                .stream()
                .map(this::toSource)
                .filter(Predicate.not(source -> repository.existsBySourceName(source.sourceName())))
                .forEach(repository::save);
    }

    private ArticleSource toSource(RssReaderSource source) {
        return new ArticleSource(
                null,
                source.sourceName(),
                source.sourceUrl(),
                null,
                LocalDateTime.now(),
                null
        );
    }
}