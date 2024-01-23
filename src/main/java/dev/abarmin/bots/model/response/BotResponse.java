package dev.abarmin.bots.model.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.Collection;
import java.util.function.Consumer;

public interface BotResponse<T extends BotResponse<T>> {
    T then(BotResponse<?> next);

    Collection<BotResponse<?>> nextActions();

    static SendMessageResponse message(SendMessage message) {
        return new SendMessageResponse(message);
    }

    static NoopResponse noop() {
        return new NoopResponse();
    }

    static CallbackResponse callback(Consumer<TelegramBot> consumer) {
        return new CallbackResponse(consumer);
    }
}
