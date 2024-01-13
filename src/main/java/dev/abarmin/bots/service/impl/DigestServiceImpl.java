package dev.abarmin.bots.service.impl;

import dev.abarmin.bots.entity.telegram.TelegramBotChat;
import dev.abarmin.bots.model.digest.Digest;
import dev.abarmin.bots.model.digest.DigestItem;
import dev.abarmin.bots.model.digest.DigestSource;
import dev.abarmin.bots.repository.ArticleRepository;
import dev.abarmin.bots.entity.rss.ArticleSource;
import dev.abarmin.bots.service.DigestService;
import dev.abarmin.bots.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DigestServiceImpl implements DigestService {
    private final ArticleRepository articleRepository;
    private final SubscriptionService subscriptionService;

    @Override
    public Digest create(TelegramBotChat chat) {
        Collection<DigestSource> sources = subscriptionService.findSubscriptions(chat)
                .stream()
                .map(this::toDigestSource)
                .collect(Collectors.toList());

        return new Digest(sources);
    }

    @Override
    public Digest create(TelegramBotChat chat, LocalDate date) {
        var sources = subscriptionService.findSubscriptions(chat)
                .stream()
                .map(source -> toDigestSource(source, date))
                .collect(Collectors.toList());

        return new Digest(sources);
    }

    private DigestSource toDigestSource(ArticleSource source) {
        final Collection<DigestItem> articles = articleRepository
                .findAllPublished(source.id(), 5)
                .stream()
                .map(article -> new DigestItem(
                        article.articleTitle(),
                        article.articleUri().toString()
                ))
                .collect(Collectors.toList());

        return new DigestSource(
                source.sourceName(),
                articles
        );
    }

    private DigestSource toDigestSource(ArticleSource source, LocalDate publicationDate) {
        final Collection<DigestItem> articles = articleRepository
                .findAllPublished(source.id(), publicationDate, 5)
                .stream()
                .map(article -> new DigestItem(
                        article.articleTitle(),
                        article.articleUri().toString()
                ))
                .collect(Collectors.toList());

        return new DigestSource(
                source.sourceName(),
                articles
        );
    }
}
