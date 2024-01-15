package dev.abarmin.bots.model.response;

import com.pengrad.telegrambot.TelegramBot;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoopResponse extends BotResponse {
    @Override
    protected void doSend(TelegramBot bot) {
        // do nothing
    }
}
