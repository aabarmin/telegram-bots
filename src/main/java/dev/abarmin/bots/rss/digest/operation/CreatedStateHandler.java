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
import dev.abarmin.bots.rss.digest.DigestBotUpdate;
import dev.abarmin.bots.rss.digest.service.RecommendationService;
import dev.abarmin.bots.rss.digest.service.SubscriptionService;
import dev.abarmin.bots.rss.reader.persistence.ArticleSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreatedStateHandler implements BotOperation {
    private final DigestOperationProcessor buildDigestOperation;
    private final ApplicationEventPublisher eventPublisher;
    private final TelegramChatService chatService;
    private final MessageSourceHelper messageSource;
    private final TelegramBot telegramBot;
    private final BotHelper helper;
    private final SubscriptionService subscriptionService;
    private final RecommendationService recommendationService;

    @Override
    public void process(Update update) {
        if (isStart(update) || isBack(update)) {
            processStart(update);
        } else if (isDigest(update)) {
            buildDigestOperation.process(update);
        } else if (isSubscriptions(update)) {
            processSubscriptions(update);
        } else if (isSubscribeToRecommended(update)) {
            processSubscribeToRecommended(update);
        } else {
            processUnknown(update);
        }
    }

    private void processSubscribeToRecommended(Update update) {
        recommendationService.findRecommendations()
                .stream()
                .map(ArticleSource::sourceUri)
                .forEach(uri -> subscriptionService.subscribe(helper.getChat(update), uri));

        telegramBot.execute(new SendMessage(
                helper.getChatId(update),
                "Great! Now you can start reading your digest!"
        ));

        eventPublisher.publishEvent(new DigestBotUpdate(
                helper.withMessage(update, "/start")
        ));
    }

    private boolean isSubscribeToRecommended(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.subscriptions-add-recommended", update)
        );
    }

    private void processSubscriptions(Update update) {
        chatService.updateStatus(helper.getChat(update), "SUBSCRIPTIONS");
        eventPublisher.publishEvent(new DigestBotUpdate(update));
    }

    private boolean isStart(Update update) {
        return StringUtils.equalsIgnoreCase(helper.getMessage(update), "/start");
    }

    private boolean isDigest(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.digest", update)
        );
    }

    private boolean isBack(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.back", update)
        );
    }

    private boolean isSubscriptions(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.subscriptions", update)
        );
    }

    private void processUnknown(Update update) {
        var message = new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.not-supported", update)
        );
        telegramBot.execute(message);
    }

    private void processStart(Update update) {
        if (hasSubscriptions(update)) {
            processStartWithSubscriptions(update);
        } else {
            processStartWithoutSubscriptions(update);
        }
    }

    private void processStartWithoutSubscriptions(Update update) {
        var sources = recommendationService.findRecommendations()
                .stream()
                .map(ArticleSource::sourceName)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining("\n"));

        var message = messageSource.getMessage("bot.digest.start-and-subscribe", update) + "\n\n" + sources;
        var telegramMessage = new SendMessage(
                helper.getChatId(update),
                message
        )
                .replyMarkup(new ReplyKeyboardMarkup(
                        new KeyboardButton[]{
                                new KeyboardButton(
                                        messageSource.getMessage("bot.digest.button.subscriptions-add-recommended", update)
                                )
                        },
                        new KeyboardButton[]{
                                new KeyboardButton(
                                        messageSource.getMessage("bot.digest.button.subscriptions", update)
                                )
                        }
                ));

        telegramBot.execute(telegramMessage);
    }

    private void processStartWithSubscriptions(Update update) {
        var message = new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.start", update)
        )
                .replyMarkup(new ReplyKeyboardMarkup(
                        new KeyboardButton[] {
                                new KeyboardButton(
                                        messageSource.getMessage("bot.digest.button.digest", update)
                                )
                        },
                        new KeyboardButton[] {
                                new KeyboardButton(
                                        messageSource.getMessage("bot.digest.button.subscriptions", update)
                                )
                        }
                ));
        telegramBot.execute(message);
    }

    private boolean hasSubscriptions(Update update) {
        return !subscriptionService.findSubscriptions(helper.getChat(update)).isEmpty();
    }

    @Override
    public boolean supports(Update update) {
        var chat = chatService.findChat(helper.getChatId(update));
        return StringUtils.equalsIgnoreCase(chat.chatStatus(), "CREATED");
    }
}
