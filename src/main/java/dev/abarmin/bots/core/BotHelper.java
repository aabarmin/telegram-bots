package dev.abarmin.bots.core;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import dev.abarmin.bots.listener.persistence.TelegramBotChat;
import dev.abarmin.bots.listener.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BotHelper {
    private final TelegramChatService chatService;

    public TelegramBotChat getChat(Update update) {
        return chatService.findChat(getChatId(update));
    }

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
