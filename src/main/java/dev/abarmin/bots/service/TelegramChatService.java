package dev.abarmin.bots.service;

import dev.abarmin.bots.rss.persistence.TelegramBotChat;

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
}
