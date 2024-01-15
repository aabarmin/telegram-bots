package dev.abarmin.bots.service;

import dev.abarmin.bots.entity.telegram.TelegramBotChat;

import java.util.Collection;

public interface TelegramChatService {
    /**
     * Retrieve a chat by id or create it if does not exist.
     *
     * @param chatId
     * @return
     */
    TelegramBotChat findChat(long chatId);

    /**
     * Update status of a given chat to the following.
     *
     * @param chat
     * @param status
     * @return
     */
    TelegramBotChat updateStatus(TelegramBotChat chat, String status);

    /**
     * Find all the chats.
     * @return a collection of chats.
     */
    Collection<TelegramBotChat> findAll();
}
