package dev.abarmin.bots.model.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SendMessageResponse extends BotResponse {
    private final SendMessage message;

    @Override
    protected void doSend(TelegramBot bot) {
        bot.execute(message);
    }
}
