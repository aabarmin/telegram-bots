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
public class SubscriptionsBackOperation implements BotOperation {
    private final BotHelper helper;
    private final MessageSourceHelper messageSource;
    private final TelegramChatService chatService;
    private final TelegramBot telegramBot;

    @Override
    public void process(Update update) {
        telegramBot.execute(new SendMessage(
                helper.getChatId(update),
                "Getting back to the main menu"
        ).replyMarkup(new ReplyKeyboardMarkup(
                new KeyboardButton(messageSource.getMessage("bot.digest.button.digest", update)),
                new KeyboardButton(messageSource.getMessage("bot.digest.button.subscriptions", update))
        )));

        chatService.updateStatus(helper.getChat(update), "CREATED");
    }

    @Override
    public boolean supports(Update update) {
        var chat = helper.getChat(update);

        return StringUtils.equalsAnyIgnoreCase(chat.chatStatus(), "SUBSCRIPTIONS", "SUBSCRIPTIONS_ADD") &&
                StringUtils.equalsIgnoreCase(helper.getMessage(update), messageSource.getMessage("bot.digest.button.back", update));
    }
}
