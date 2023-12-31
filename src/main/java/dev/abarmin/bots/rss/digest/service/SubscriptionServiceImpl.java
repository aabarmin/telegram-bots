package dev.abarmin.bots.rss.digest.service;

import dev.abarmin.bots.listener.persistence.TelegramBotChat;
import dev.abarmin.bots.rss.digest.persistence.ArticleSubscription;
import dev.abarmin.bots.rss.digest.persistence.ArticleSubscriptionRepository;
import dev.abarmin.bots.rss.reader.persistence.ArticleSource;
import dev.abarmin.bots.rss.reader.persistence.ArticleSourceRepository;
import dev.abarmin.bots.rss.reader.service.ArticleSourceService;
import dev.abarmin.bots.rss.reader.service.RssArticleReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final ArticleSourceService sourceService;
    private final RssArticleReader articleReader;
    private final ArticleSubscriptionRepository repository;
    private final ArticleSourceRepository sourceRepository;

    @Override
    public Collection<ArticleSource> findSubscriptions(TelegramBotChat chat) {
        return repository.findAllByChatId(AggregateReference.to(chat.chatId()))
                .stream()
                .map(ArticleSubscription::sourceId)
                .map(AggregateReference::getId)
                .map(sourceRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(ArticleSource::sourceName))
                .collect(Collectors.toList());
    }

    @Override
    public ArticleSubscription subscribe(TelegramBotChat chat, URI rssUri) {
        var articleSource = sourceService.findOrCreate(rssUri);
        articleReader.read(articleSource);

        return repository.findByChatIdAndSourceId(
                        AggregateReference.to(chat.chatId()),
                        AggregateReference.to(articleSource.id())
                )
                .orElseGet(() -> repository.save(new ArticleSubscription(
                        AggregateReference.to(chat.chatId()),
                        AggregateReference.to(articleSource.id())
                )));
    }
}
