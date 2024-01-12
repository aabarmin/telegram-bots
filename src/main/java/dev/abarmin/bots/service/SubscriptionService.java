package dev.abarmin.bots.service;

import dev.abarmin.bots.listener.persistence.TelegramBotChat;
import dev.abarmin.bots.rss.digest.persistence.ArticleSubscription;
import dev.abarmin.bots.rss.reader.persistence.ArticleSource;

import java.net.URI;
import java.util.Collection;

public interface SubscriptionService {
    /**
     * Unsubscribe from a given source.
     *
     * @param chat
     * @param source
     * @return
     */
    boolean unsubscribe(TelegramBotChat chat, ArticleSource source);

    /**
     * Find all the subscriptions for the given chat.
     *
     * @param chat
     * @return
     */
    Collection<ArticleSource> findSubscriptions(TelegramBotChat chat);

    /**
     * Create a subscription for a given chat.
     *
     * @param chat
     * @param rssUri
     * @return
     */
    ArticleSubscription subscribe(TelegramBotChat chat, URI rssUri);
}
