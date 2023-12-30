package dev.abarmin.bots.core;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public interface BotOperation {
    void process(Update update);

    default boolean supports(Update update) {
        return false;
    }

    default long getChatId(Update update) {
        return Optional.of(update)
                .map(Update::message)
                .map(Message::chat)
                .map(Chat::id)
                .orElseThrow();
    }

    default String getMessage(Update update) {
        return Optional.of(update)
                .map(Update::message)
                .map(Message::text)
                .orElse(StringUtils.EMPTY);
    }
}
