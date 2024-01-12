package dev.abarmin.bots.rss.digest.operation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.core.BotHelper;
import dev.abarmin.bots.core.BotOperation;
import dev.abarmin.bots.core.MessageSourceHelper;
import dev.abarmin.bots.service.TelegramChatService;
import dev.abarmin.bots.rss.digest.DigestBotUpdate;
import dev.abarmin.bots.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionsStateHandler implements BotOperation {
    private final ApplicationEventPublisher eventPublisher;
    private final BotHelper helper;
    private final TelegramBot telegramBot;
    private final MessageSourceHelper messageSource;
    private final TelegramChatService chatService;
    private final SubscriptionService subscriptionService;

    @Override
    public void process(Update update) {
        if (isManageSubscriptions(update)) {
            processManageSubscriptions(update);
        } else if (isBack(update)) {
            processBack(update);
        } else if (isAddSubscription(update)) {
            processAddSubscription(update);
        } else if (isDeleteSubscription(update)) {
            processDeleteSubscription(update);
        }
    }

    private void processDeleteSubscription(Update update) {
        chatService.updateStatus(helper.getChat(update), "SUBSCRIPTIONS_DELETE");
        eventPublisher.publishEvent(new DigestBotUpdate(update));
    }

    private boolean isDeleteSubscription(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.subscriptions-delete", update)
        );
    }

    private void processAddSubscription(Update update) {
        chatService.updateStatus(helper.getChat(update), "SUBSCRIPTIONS_ADD");
        eventPublisher.publishEvent(new DigestBotUpdate(update));
    }

    private void processBack(Update update) {
        chatService.updateStatus(helper.getChat(update), "CREATED");
        telegramBot.execute(new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.button.subscriptions-back-success", update)
        ));
        eventPublisher.publishEvent(new DigestBotUpdate(update));
    }

    private boolean isBack(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.back", update)
        );
    }

    private boolean isManageSubscriptions(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.subscriptions", update)
        );
    }

    private boolean isAddSubscription(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.subscriptions-add", update)
        );
    }

    private void processManageSubscriptions(Update update) {
        var sources = subscriptionService.findSubscriptions(helper.getChat(update))
                .stream()
                .map(source -> String.format("%s", source.sourceName()))
                .collect(Collectors.joining("\n"));

        var message = new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.button.subscriptions-manage", update)
                        + "\n\n"
                        + sources
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
        telegramBot.execute(message);
    }

    @Override
    public boolean supports(Update update) {
        var chat = helper.getChat(update);
        return StringUtils.equalsIgnoreCase(chat.chatStatus(), "SUBSCRIPTIONS");
    }
}
