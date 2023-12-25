package dev.abarmin.bots.rssreader.service;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import dev.abarmin.bots.rssreader.persistence.Article;
import dev.abarmin.bots.rssreader.persistence.ArticleSource;
import dev.abarmin.bots.rssreader.persistence.ArticleSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssArticleReader implements ArticleReader {
    private final RssReader reader;
    private final ArticleSourceRepository repository;

    @Override
    @SneakyThrows
    public Collection<Article> read(ArticleSource source) {
        log.info("Reading articles from {}", source.sourceUrl());
        final Collection<Article> articles = reader.read(source.sourceUrl())
                .map(item -> toArticle(item, source))
                .collect(Collectors.toList());

        repository.save(source.withLastUpdated(LocalDateTime.now()));

        return articles;
    }

    private Article toArticle(Item item, ArticleSource source) {
        return new Article(
                null,
                AggregateReference.to(source.id()),
                item.getTitle().orElse("No Title"),
                item.getLink().orElse("No URL"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }
}
