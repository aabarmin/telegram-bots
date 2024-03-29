package dev.abarmin.bots.repository;

import dev.abarmin.bots.entity.rss.ArticleSubscription;
import dev.abarmin.bots.entity.telegram.TelegramBotChat;
import dev.abarmin.bots.entity.rss.ArticleSource;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface ArticleSubscriptionRepository extends ListCrudRepository<ArticleSubscription, Integer> {
    Collection<ArticleSubscription> findAllByChatId(long chatId);

    Optional<ArticleSubscription> findByChatIdAndSourceId(
            long chatId,
            AggregateReference<ArticleSource, Integer> sourceId
    );
}
