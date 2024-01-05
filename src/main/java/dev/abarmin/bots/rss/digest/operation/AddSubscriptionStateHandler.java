package dev.abarmin.bots.rss.digest.operation;

import com.apptasticsoftware.rssreader.RssReader;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.core.BotHelper;
import dev.abarmin.bots.core.BotOperation;
import dev.abarmin.bots.core.MessageSourceHelper;
import dev.abarmin.bots.listener.service.TelegramChatService;
import dev.abarmin.bots.rss.digest.DigestBotUpdate;
import dev.abarmin.bots.rss.digest.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class AddSubscriptionStateHandler implements BotOperation {
    private final ApplicationEventPublisher eventPublisher;
    private final BotHelper helper;
    private final TelegramBot telegramBot;
    private final TelegramChatService chatService;
    private final MessageSourceHelper messageSource;
    private final RssReader rssReader;
    private final SubscriptionService subscriptionService;

    @Override
    public void process(Update update) {
        if (isAddSubscription(update)) {
            processAddSubscription(update);
        } else if (isBack(update)) {
            processBack(update);
        } else if (isValidRss(update)) {
            processSubscription(update);
        } else {
            processInvalidRss(update);
        }
    }

    private void processAddSubscription(Update update) {
        telegramBot.execute(new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.button.subscriptions-add-request", update)
        ).replyMarkup(new ReplyKeyboardRemove()));
    }

    private boolean isAddSubscription(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.subscriptions-add", update)
        );
    }

    private void processInvalidRss(Update update) {
        telegramBot.execute(new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.button.subscriptions-add-invalid-url", update)
        ));
    }

    private void processSubscription(Update update) {
        var message = helper.getMessage(update);
        subscriptionService.subscribe(helper.getChat(update), URI.create(message));
        chatService.updateStatus(helper.getChat(update), "SUBSCRIPTIONS");

        var sendMessage = new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.button.subscriptions-added", update)
        )
                .replyMarkup(new ReplyKeyboardMarkup(
                        new KeyboardButton[]{
                                new KeyboardButton(messageSource.getMessage("bot.digest.button.back", update)),
                        },
                        new KeyboardButton[]{
                                new KeyboardButton(messageSource.getMessage("bot.digest.button.subscriptions-add", update)),
                                new KeyboardButton(messageSource.getMessage("bot.digest.button.subscriptions-delete", update))
                        }
                ));
        telegramBot.execute(sendMessage);
    }

    private void processBack(Update update) {
        chatService.updateStatus(helper.getChat(update), "SUBSCRIPTIONS");
        eventPublisher.publishEvent(new DigestBotUpdate(update));
    }

    private boolean isBack(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.back", update)
        );
    }

    private boolean isValidRss(Update update) {
        var message = helper.getMessage(update);
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
