package dev.abarmin.bots.service.digest;

import com.pengrad.telegrambot.model.Update;
import dev.abarmin.bots.entity.telegram.TelegramBotChat;
import dev.abarmin.bots.model.request.BotRequest;

public class BotRequestFactory {
    public BotRequest newRequest(final String message, final TelegramBotChat chat) {
        return new BotRequest(
                new Update(),
                chat,
                message
        );
    }
}
