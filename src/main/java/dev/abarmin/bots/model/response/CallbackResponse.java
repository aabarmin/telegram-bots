package dev.abarmin.bots.model.response;

import com.google.common.collect.Lists;
import com.pengrad.telegrambot.TelegramBot;

import java.util.Collection;
import java.util.function.Consumer;

public record CallbackResponse(
        Consumer<TelegramBot> callback,
        Collection<BotResponse<?>> nextActions
) implements BotResponse<CallbackResponse> {
    public CallbackResponse(Consumer<TelegramBot> callback) {
        this(callback, Lists.newArrayList());
    }

    @Override
    public CallbackResponse then(BotResponse<?> next) {
        this.nextActions.add(next);
        return this;
    }
}
