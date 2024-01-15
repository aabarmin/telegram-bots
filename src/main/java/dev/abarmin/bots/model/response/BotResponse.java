package dev.abarmin.bots.model.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.function.Consumer;

public abstract class BotResponse {
    private BotResponse nextAction = null;

    public void send(TelegramBot bot) {
        doSend(bot);
        if (nextAction != null) {
            nextAction.send(bot);
        }
    }

    protected abstract void doSend(TelegramBot bot);

    public BotResponse then(BotResponse next) {
        this.nextAction = next;
        return this;
    }

    public static BotResponse message(SendMessage message) {
        return new SendMessageResponse(message);
    }

    public static BotResponse noop() {
        return new NoopResponse();
    }

    public static BotResponse callback(Consumer<TelegramBot> consumer) {
        return new CallbackResponse(consumer);
    }
}
