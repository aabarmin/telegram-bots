package dev.abarmin.bots.model.response;

import com.google.common.collect.Lists;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

public record SendMessageResponse(
        SendMessage message,
        Collection<BotResponse<?>> nextActions
) implements BotResponse<SendMessageResponse> {
    public SendMessageResponse(SendMessage message) {
        this(message, Lists.newArrayList());
    }

    @Override
    public SendMessageResponse then(BotResponse<?> next) {
        this.nextActions.add(next);
        return this;
    }
}
