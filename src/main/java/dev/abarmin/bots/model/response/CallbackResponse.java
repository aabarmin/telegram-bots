package dev.abarmin.bots.model.response;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class CallbackResponse extends BotResponse {
    private final Consumer<TelegramBot> callback;

    @Override
    protected void doSend(TelegramBot bot) {
        callback.accept(bot);
    }
}
