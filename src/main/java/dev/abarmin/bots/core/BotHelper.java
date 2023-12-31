package dev.abarmin.bots.core;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BotHelper {
    public long getChatId(Update update) {
        return Optional.of(update)
                .map(Update::message)
                .map(Message::chat)
                .map(Chat::id)
                .orElseThrow();
    }

    public String getMessage(Update update) {
        return Optional.of(update)
                .map(Update::message)
                .map(Message::text)
                .orElse(StringUtils.EMPTY);
    }
}
