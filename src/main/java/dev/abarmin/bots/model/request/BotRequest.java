package dev.abarmin.bots.model.request;

import com.pengrad.telegrambot.model.Update;
import dev.abarmin.bots.entity.telegram.TelegramBotChat;

public record BotRequest(
    Update update,
    TelegramBotChat chat,
    String message
) {

    public long chatId() {
        return chat().chatId();
    }

    public String chatStatus() {
        return chat().chatStatus();
    }
}
