package dev.abarmin.bots.rss.digest.operation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.core.BotHelper;
import dev.abarmin.bots.core.BotOperation;
import dev.abarmin.bots.core.MessageSourceHelper;
import dev.abarmin.bots.listener.persistence.TelegramBotChat;
import dev.abarmin.bots.listener.service.TelegramChatService;
import dev.abarmin.bots.rss.digest.service.SubscriptionService;
import dev.abarmin.bots.rss.reader.persistence.ArticleSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UnsubscribeOperation implements BotOperation {
    private final BotHelper helper;
    private final TelegramBot telegramBot;
    private final TelegramChatService chatService;
    private final MessageSourceHelper messageSource;
    private final SubscriptionService subscriptionService;

    @Override
    public void process(Update update) {
        if (isValidNumber(update)) {
            // unsubscribing
            var subscriptions = subscriptions(helper.getChat(update));
            var articleSource = subscriptions.get(helper.getMessage(update));
            subscriptionService.unsubscribe(helper.getChat(update), articleSource);

            var msg = subscriptionService.findSubscriptions(helper.getChat(update))
                    .stream()
                    .map(ArticleSource::sourceName)
                    .collect(Collectors.joining("\n"));

            var message = new SendMessage(
                    helper.getChatId(update),
                    "Unsubscribed. \nManaging subscriptions, you're subscribed to the following sources: \n\n" + msg
            ).replyMarkup(new ReplyKeyboardMarkup(
                    new KeyboardButton(messageSource.getMessage("bot.digest.button.back", update)),
                    new KeyboardButton(messageSource.getMessage("bot.digest.button.subscriptions-add", update)),
                    new KeyboardButton(messageSource.getMessage("bot.digest.button.subscriptions-delete", update))
            ));

            chatService.updateStatus(
                    chatService.findChat(helper.getChatId(update)),
                    "SUBSCRIPTIONS"
            );

            telegramBot.execute(message);
        } else {
            // not a valid
            telegramBot.execute(new SendMessage(
                    helper.getChatId(update),
                    "Doesn't look like a valid number, type back to return"
            ));
        }
    }

    private boolean isValidNumber(Update update) {
        return subscriptions(helper.getChat(update))
                .containsKey(helper.getMessage(update));
    }

    private Map<String, ArticleSource> subscriptions(TelegramBotChat chat) {
        var subscriptions = new HashMap<String, ArticleSource>();
        int index = 1;
        for (ArticleSource subscription : subscriptionService.findSubscriptions(chat)) {
            subscriptions.put(String.valueOf(index++), subscription);
        }
        return subscriptions;
    }

    @Override
    public boolean supports(Update update) {
        var chat = helper.getChat(update);

        return StringUtils.equalsIgnoreCase(chat.chatStatus(), "SUBSCRIPTIONS_DELETE");
    }
}
