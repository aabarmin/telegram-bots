package dev.abarmin.bots.rss.digest.persistence;

import dev.abarmin.bots.rss.persistence.TelegramBotChat;
import dev.abarmin.bots.rss.reader.persistence.ArticleSource;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface ArticleSubscriptionRepository extends ListCrudRepository<ArticleSubscription, Integer> {
    Collection<ArticleSubscription> findAllByChatId(AggregateReference<TelegramBotChat, Long> chatId);

    Optional<ArticleSubscription> findByChatIdAndSourceId(
            AggregateReference<TelegramBotChat, Long> chatId,
            AggregateReference<ArticleSource, Integer> sourceId
    );
}
