package dev.abarmin.bots.service;

import dev.abarmin.bots.entity.TelegramBotChat;
import dev.abarmin.bots.model.digest.Digest;
import dev.abarmin.bots.model.digest.DigestItem;
import dev.abarmin.bots.model.digest.DigestSource;
import dev.abarmin.bots.repository.ArticleRepository;
import dev.abarmin.bots.entity.ArticleSource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DigestBuildingService {
    private final ArticleRepository articleRepository;
    private final SubscriptionService subscriptionService;

    public Digest create(TelegramBotChat chat) {
        Collection<DigestSource> sources = subscriptionService.findSubscriptions(chat)
                .stream()
                .map(this::toDigestSource)
                .collect(Collectors.toList());

        return new Digest(sources);
    }

    private DigestSource toDigestSource(ArticleSource source) {
        AggregateReference<ArticleSource, Integer> reference = AggregateReference.to(source.id());
        Collection<DigestItem> articles = articleRepository
                .findAllByArticleSourceOrderByArticleAddedDesc(reference, Limit.of(5))
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
