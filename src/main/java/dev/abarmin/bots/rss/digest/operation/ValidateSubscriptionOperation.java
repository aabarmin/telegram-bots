package dev.abarmin.bots.rss.digest.operation;

import com.apptasticsoftware.rssreader.RssReader;
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

import java.net.URI;

@Component
@RequiredArgsConstructor
public class ValidateSubscriptionOperation implements BotOperation {
    private final BotHelper helper;
    private final TelegramBot telegramBot;
    private final RssReader rssReader;
    private final SubscriptionService subscriptionService;
    private final TelegramChatService chatService;
    private final MessageSourceHelper messageSource;

    @Override
    public void process(Update update) {
        var message = helper.getMessage(update);

        if (isValidRss(message)) {
            // add subscription
            subscriptionService.subscribe(helper.getChat(update), URI.create(message));
            chatService.updateStatus(helper.getChat(update), "SUBSCRIPTIONS");

            var sendMessage = new SendMessage(
                    helper.getChatId(update),
                    "Added to your subscriptions"
            )
                    .replyMarkup(new ReplyKeyboardMarkup(
                            new KeyboardButton(messageSource.getMessage("bot.digest.button.back", update)),
                            new KeyboardButton(messageSource.getMessage("bot.digest.button.subscriptions-add", update)),
                            new KeyboardButton("Delete subscription")
                    ));
            telegramBot.execute(sendMessage);
        } else {
            // not a valid subscription
            telegramBot.execute(new SendMessage(
                    helper.getChatId(update),
                    "Provided URL is not a valid RSS. Enter URL of a subscription or Back to return"
            ));
        }
    }

    private boolean isValidRss(String message) {
        try {
            return rssReader.read(message)
                    .findFirst()
                    .isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean supports(Update update) {
        var chat = helper.getChat(update);

        return StringUtils.equalsIgnoreCase(chat.chatStatus(), "SUBSCRIPTIONS_ADD");
    }
}
