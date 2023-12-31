package dev.abarmin.bots.rss.digest.operation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.core.BotHelper;
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
    private final MessageSourceHelper messageSource;
    private final TelegramBot telegramBot;
    private final BotHelper helper;

    @Override
    public void process(Update update) {
        var message = new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.start", update)
        )
                .replyMarkup(new ReplyKeyboardMarkup(
                        new KeyboardButton(
                                messageSource.getMessage("bot.digest.button.digest", update)
                        )
                ));
        telegramBot.execute(message);
    }

    @Override
    public boolean supports(Update update) {
        var chat = chatService.findChat(helper.getChatId(update));
        return StringUtils.equalsIgnoreCase(
                chat.chatStatus(),
                "CREATED"
        ) && StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                "/start"
        );
    }
}
