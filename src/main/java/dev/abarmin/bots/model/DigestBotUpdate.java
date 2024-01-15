package dev.abarmin.bots.model;

import com.pengrad.telegrambot.model.Update;
import dev.abarmin.bots.model.request.BotRequest;

public record DigestBotUpdate(
        Update update
) {
    public DigestBotUpdate(BotRequest request) {
        this(request.update());
    }
}
