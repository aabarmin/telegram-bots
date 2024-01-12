package dev.abarmin.bots.service;

import com.pengrad.telegrambot.model.Update;

public interface TelegramUpdateService {
    /**
     * Check if a given update is processed.
     *
     * @param update
     * @return
     */
    boolean isProcessed(Update update);

    /**
     * Save a given update as processed.
     *
     * @param update
     * @return
     */
    Update save(Update update);
}
