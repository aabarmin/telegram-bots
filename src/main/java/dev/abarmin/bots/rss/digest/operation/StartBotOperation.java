package dev.abarmin.bots.rss.digest.operation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.core.BotOperation;
import dev.abarmin.bots.core.MessageSourceHelper;
import dev.abarmin.bots.listener.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartBotOperation implements BotOperation {
    private final TelegramChatService chatService;
    private final MessageSourceHelper helper;
    private final TelegramBot telegramBot;

    @Override
    public void process(Update update) {
        telegramBot.execute(new SendMessage(
                getChatId(update),
                helper.getMessage(
                        "bot.digest.start",
                        update
                )
        ));
    }

    @Override
    public boolean supports(Update update) {
        var chat = chatService.findChat(getChatId(update));
        return StringUtils.equalsIgnoreCase(
                chat.chatStatus(),
                "CREATED"
        ) && StringUtils.equalsIgnoreCase(
                getMessage(update),
                "/start"
        );
    }
}
