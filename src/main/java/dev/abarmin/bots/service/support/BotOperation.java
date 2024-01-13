package dev.abarmin.bots.service.support;

import com.pengrad.telegrambot.model.Update;

public interface BotOperation {
    void process(Update update);

    default boolean supports(Update update) {
        return false;
    }
}
