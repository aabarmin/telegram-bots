package dev.abarmin.bots.service.impl;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import dev.abarmin.bots.entity.rss.Article;
import dev.abarmin.bots.entity.rss.ArticleSource;
import dev.abarmin.bots.repository.ArticleSourceRepository;
import dev.abarmin.bots.service.ArticleReaderService;
import dev.abarmin.bots.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssArticleReaderServiceImpl implements ArticleReaderService {
    private final RssReader reader;
    private final ArticleService articleService;
    private final ArticleSourceRepository repository;

    @Override
    @SneakyThrows
    public Collection<Article> read(ArticleSource source) {
        log.info("Reading articles from {}", source.sourceUri());
        final Collection<Article> articles = reader.read(source.sourceUri().toString())
                .map(item -> toArticle(item, source))
                .map(articleService::save)
                .collect(Collectors.toList());

        repository.save(source.withLastUpdated(LocalDateTime.now()));

        return articles;
    }

    private Article toArticle(Item item, ArticleSource source) {
        return new Article(
                null,
                AggregateReference.to(source.id()),
                item.getTitle().orElse("No Title"),
                item.getLink()
                        .map(URI::create)
                        .orElse(null),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }
}
