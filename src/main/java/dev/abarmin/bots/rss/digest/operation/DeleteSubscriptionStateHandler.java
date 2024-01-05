package dev.abarmin.bots.rss.digest.operation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.core.BotHelper;
import dev.abarmin.bots.core.BotOperation;
import dev.abarmin.bots.core.MessageSourceHelper;
import dev.abarmin.bots.listener.persistence.TelegramBotChat;
import dev.abarmin.bots.listener.service.TelegramChatService;
import dev.abarmin.bots.rss.digest.DigestBotUpdate;
import dev.abarmin.bots.rss.digest.service.SubscriptionService;
import dev.abarmin.bots.rss.reader.persistence.ArticleSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeleteSubscriptionStateHandler implements BotOperation {
    private final BotHelper helper;
    private final ApplicationEventPublisher eventPublisher;
    private final TelegramBot telegramBot;
    private final TelegramChatService chatService;
    private final MessageSourceHelper messageSource;
    private final SubscriptionService subscriptionService;

    @Override
    public void process(Update update) {
        if (isDeleteSubscription(update)) {
            processDeleteSubscription(update);
        } else if (isBack(update)) {
            processBack(update);
        } else if (isValidNumber(update)) {
            processUnsubscribe(update);
        } else {
            processInvalidMessage(update);
        }
    }

    private void processInvalidMessage(Update update) {
        telegramBot.execute(new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.button.subscriptions-delete-invalid-number", update)
        ));
    }

    private void processUnsubscribe(Update update) {
        var subscriptions = subscriptions(helper.getChat(update));
        var articleSource = subscriptions.get(helper.getMessage(update));

        subscriptionService.unsubscribe(helper.getChat(update), articleSource);
        chatService.updateStatus(helper.getChat(update), "SUBSCRIPTIONS");

        telegramBot.execute(new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.button.subscriptions-delete-success", update)
        ));

        eventPublisher.publishEvent(new DigestBotUpdate(
                helper.withMessage(update, messageSource.getMessage("bot.digest.button.subscriptions", update))
        ));
    }

    private boolean isValidNumber(Update update) {
        return subscriptions(helper.getChat(update))
                .containsKey(helper.getMessage(update));
    }

    private void processBack(Update update) {
        chatService.updateStatus(helper.getChat(update), "SUBSCRIPTIONS");
        eventPublisher.publishEvent(new DigestBotUpdate(update));
    }

    private void processDeleteSubscription(Update update) {
        var subscriptions = subscriptions(helper.getChat(update))
                .entrySet()
                .stream()
                .map(entry -> String.format("%s. %s", entry.getKey(), entry.getValue().sourceName()))
                .collect(Collectors.joining("\n"));

        var message = new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.button.subscriptions-delete-request", update)
                        + "\n\n"
                        + subscriptions
        ).replyMarkup(new ReplyKeyboardRemove());

        telegramBot.execute(message);
    }

    private boolean isDeleteSubscription(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.subscriptions-delete", update)
        );
    }

    private boolean isBack(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.back", update)
        );
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
