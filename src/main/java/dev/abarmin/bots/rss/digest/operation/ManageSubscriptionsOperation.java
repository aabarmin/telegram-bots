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
import dev.abarmin.bots.rss.digest.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ManageSubscriptionsOperation implements BotOperation {
    private final BotHelper helper;
    private final TelegramBot telegramBot;
    private final MessageSourceHelper messageSource;
    private final TelegramChatService chatService;
    private final SubscriptionService subscriptionService;

    @Override
    public void process(Update update) {
        chatService.updateStatus(
                chatService.findChat(helper.getChatId(update)),
                "SUBSCRIPTIONS"
        );

        var sources = subscriptionService.findSubscriptions(helper.getChat(update))
                .stream()
                .map(source -> String.format("%s", source.sourceName()))
                .collect(Collectors.joining("\n"));

        var message = new SendMessage(
                helper.getChatId(update),
                "Managing subscriptions, you're subscribed to the following sources: \n\n" + sources
        )
                .replyMarkup(new ReplyKeyboardMarkup(
                        new KeyboardButton(messageSource.getMessage("bot.digest.button.back", update)),
                        new KeyboardButton(messageSource.getMessage("bot.digest.button.subscriptions-add", update)),
                        new KeyboardButton("Delete subscription")
                ));
        telegramBot.execute(message);
    }

    @Override
    public boolean supports(Update update) {
        var chat = helper.getChat(update);

        return StringUtils.equalsIgnoreCase(chat.chatStatus(), "CREATED") &&
                StringUtils.equalsIgnoreCase(helper.getMessage(update), messageSource.getMessage("bot.digest.button.subscriptions", update));
    }
}
