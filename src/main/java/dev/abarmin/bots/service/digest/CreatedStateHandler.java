package dev.abarmin.bots.service.digest;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.entity.rss.ArticleSource;
import dev.abarmin.bots.model.request.BotRequest;
import dev.abarmin.bots.model.DigestBotUpdate;
import dev.abarmin.bots.service.RecommendationService;
import dev.abarmin.bots.service.SubscriptionService;
import dev.abarmin.bots.service.TelegramChatService;
import dev.abarmin.bots.service.digest.processor.DigestOperationProcessor;
import dev.abarmin.bots.service.digest.processor.WhatsNewOperationProcessor;
import dev.abarmin.bots.service.support.*;
import dev.abarmin.bots.model.response.BotResponse;
import dev.abarmin.bots.model.response.NoopResponse;
import dev.abarmin.bots.model.response.SendMessageResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreatedStateHandler implements BotOperation {
    private final DigestOperationProcessor buildDigestOperation;
    private final WhatsNewOperationProcessor whatsNewOperation;
    private final ApplicationEventPublisher eventPublisher;
    private final TelegramChatService chatService;
    private final MessageSourceHelper messageSource;
    private final BotHelper helper;
    private final SubscriptionService subscriptionService;
    private final RecommendationService recommendationService;

    @Override
    public BotResponse process(BotRequest request) {
        if (isStart(request) || isBack(request)) {
            return processStart(request);
        } else if (isDigest(request)) {
            return buildDigestOperation.process(request);
        } else if (isWhatsNewToday(request)) {
            return whatsNewOperation.process(request);
        } else if (isSubscriptions(request)) {
            return processSubscriptions(request);
        } else if (isSubscribeToRecommended(request)) {
            return processSubscribeToRecommended(request);
        } else {
            return processUnknown(request);
        }
    }

    private BotResponse processSubscribeToRecommended(BotRequest request) {
        recommendationService.findRecommendations()
                .stream()
                .map(ArticleSource::sourceUri)
                .forEach(uri -> subscriptionService.subscribe(request.chat(), uri));

        eventPublisher.publishEvent(new DigestBotUpdate(
                helper.withMessage(request, "/start")
        ));

        return new SendMessageResponse(new SendMessage(
                request.chatId(),
                messageSource.getMessage("bot.digest.button.subscriptions-add-recommended-success", request)
        ));
    }

    private boolean isSubscribeToRecommended(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.subscriptions-add-recommended", request)
        );
    }

    private BotResponse processSubscriptions(BotRequest request) {
        chatService.updateStatus(request.chat(), "SUBSCRIPTIONS");
        eventPublisher.publishEvent(new DigestBotUpdate(request));
        return new NoopResponse();
    }

    private boolean isStart(BotRequest request) {
        return StringUtils.equalsIgnoreCase(request.message(), "/start");
    }

    private boolean isDigest(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.digest", request)
        );
    }

    private boolean isBack(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.back", request)
        );
    }

    private boolean isSubscriptions(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.subscriptions", request)
        );
    }

    private boolean isWhatsNewToday(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.whats-new-today", request)
        );
    }

    private BotResponse processUnknown(BotRequest request) {
        var message = new SendMessage(
                request.chatId(),
                messageSource.getMessage("bot.digest.not-supported", request)
        );
        return new SendMessageResponse(message);
    }

    private BotResponse processStart(BotRequest request) {
        if (hasSubscriptions(request)) {
            return processStartWithSubscriptions(request);
        } else {
            return processStartWithoutSubscriptions(request);
        }
    }

    private BotResponse processStartWithoutSubscriptions(BotRequest request) {
        var sources = recommendationService.findRecommendations()
                .stream()
                .map(ArticleSource::sourceName)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining("\n"));

        var message = messageSource.getMessage("bot.digest.start-and-subscribe", request) + "\n\n" + sources;
        var telegramMessage = new SendMessage(
                request.chatId(),
                message
        )
                .replyMarkup(new ReplyKeyboardMarkup(
                        new KeyboardButton[]{
                                new KeyboardButton(
                                        messageSource.getMessage("bot.digest.button.subscriptions-add-recommended", request)
                                )
                        },
                        new KeyboardButton[]{
                                new KeyboardButton(
                                        messageSource.getMessage("bot.digest.button.subscriptions", request)
                                )
                        }
                ).resizeKeyboard(true));

        return new SendMessageResponse(telegramMessage);
    }

    private BotResponse processStartWithSubscriptions(BotRequest request) {
        var message = new SendMessage(
                request.chatId(),
                messageSource.getMessage("bot.digest.start", request)
        )
                .replyMarkup(new ReplyKeyboardMarkup(
                        new KeyboardButton[] {
                                new KeyboardButton(
                                        messageSource.getMessage("bot.digest.button.digest", request)
                                ),
                                new KeyboardButton(
                                        messageSource.getMessage("bot.digest.button.whats-new-today", request)
                                )
                        },
                        new KeyboardButton[] {
                                new KeyboardButton(
                                        messageSource.getMessage("bot.digest.button.subscriptions", request)
                                )
                        }
                ).resizeKeyboard(true));
        return new SendMessageResponse(message);
    }

    private boolean hasSubscriptions(BotRequest request) {
        return !subscriptionService.findSubscriptions(request.chat()).isEmpty();
    }

    @Override
    public boolean supports(BotRequest request) {
        return StringUtils.equalsIgnoreCase(request.chatStatus(), "CREATED");
    }
}
