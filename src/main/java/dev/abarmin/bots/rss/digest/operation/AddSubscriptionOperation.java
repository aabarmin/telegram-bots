package dev.abarmin.bots.rss.digest.operation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
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
public class AddSubscriptionOperation implements BotOperation {
    private final BotHelper helper;
    private final TelegramBot telegramBot;
    private final TelegramChatService chatService;
    private final MessageSourceHelper messageSource;

    @Override
    public void process(Update update) {
        var message = new SendMessage(
                helper.getChatId(update),
                "Enter URL of a subscription or Back to return"
        )
                .replyMarkup(new ReplyKeyboardRemove());

        chatService.updateStatus(helper.getChat(update), "SUBSCRIPTIONS_ADD");
        telegramBot.execute(message);
    }

    @Override
    public boolean supports(Update update) {
        var chat = helper.getChat(update);
        var message = helper.getMessage(update);

        return StringUtils.equalsIgnoreCase(chat.chatStatus(), "SUBSCRIPTIONS") &&
                StringUtils.equalsIgnoreCase(message, messageSource.getMessage("bot.digest.button.subscriptions-add", update));
    }
}
