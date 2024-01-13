package dev.abarmin.bots.service;

import dev.abarmin.bots.entity.telegram.TelegramBotChat;
import dev.abarmin.bots.model.digest.Digest;

import java.time.LocalDate;

public interface DigestService {
    /**
     * Create a digest based on subscriptions of a provided chat.
     *
     * @param chat that requests subscription digest.
     * @return a digest object.
     */
    Digest create(TelegramBotChat chat);

    /**
     * Create a digest based on subscriptions of a provided chat and date.
     *
     * @param chat that requests subscription digest.
     * @param date of digest.
     * @return a digest object.
     */
    Digest create(TelegramBotChat chat, LocalDate date);
}
