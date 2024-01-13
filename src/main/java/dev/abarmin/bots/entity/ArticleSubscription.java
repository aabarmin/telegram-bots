package dev.abarmin.bots.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ARTICLE_SUBSCRIPTIONS")
public record ArticleSubscription(
        @Column("ID") @Id Integer id,
        @Column("CHAT_ID") AggregateReference<TelegramBotChat, Long> chatId,
        @Column("ARTICLE_SOURCE_ID") AggregateReference<ArticleSource, Integer> sourceId
) {
    public ArticleSubscription(
            AggregateReference<TelegramBotChat, Long> chatId,
            AggregateReference<ArticleSource, Integer> sourceId
    ) {
        this(null, chatId, sourceId);
    }
}
