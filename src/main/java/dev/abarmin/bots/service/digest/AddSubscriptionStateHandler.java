package dev.abarmin.bots.service.digest;

import com.apptasticsoftware.rssreader.RssReader;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.model.request.BotRequest;
import dev.abarmin.bots.model.DigestBotUpdate;
import dev.abarmin.bots.service.SubscriptionService;
import dev.abarmin.bots.service.TelegramChatService;
import dev.abarmin.bots.service.support.*;
import dev.abarmin.bots.model.response.BotResponse;
import dev.abarmin.bots.model.response.SendMessageResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class AddSubscriptionStateHandler implements BotOperation {
    private final ApplicationEventPublisher eventPublisher;
    private final TelegramChatService chatService;
    private final MessageSourceHelper messageSource;
    private final RssReader rssReader;
    private final SubscriptionService subscriptionService;

    @Override
    public BotResponse process(BotRequest request) {
        if (isAddSubscription(request)) {
            return processAddSubscription(request);
        } else if (isBack(request)) {
            return processBack(request);
        } else if (isValidRss(request)) {
            return processSubscription(request);
        } else {
            return processInvalidRss(request);
        }
    }

    private BotResponse processAddSubscription(BotRequest request) {
        var message = new SendMessage(
                request.chatId(),
                messageSource.getMessage("bot.digest.button.subscriptions-add-request", request)
        )
                .replyMarkup(new ReplyKeyboardRemove());

        return new SendMessageResponse(message);
    }

    private boolean isAddSubscription(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.subscriptions-add", request)
        );
    }

    private BotResponse processInvalidRss(BotRequest request) {
        var message = new SendMessage(
                request.chatId(),
                messageSource.getMessage("bot.digest.button.subscriptions-add-invalid-url", request)
        );

        return new SendMessageResponse(message);
    }

    private BotResponse processSubscription(BotRequest request) {
        subscriptionService.subscribe(request.chat(), URI.create(request.message()));
        chatService.updateStatus(request.chat(), "SUBSCRIPTIONS");

        var sendMessage = new SendMessage(
                request.chatId(),
                messageSource.getMessage("bot.digest.button.subscriptions-added", request)
        )
                .replyMarkup(new ReplyKeyboardMarkup(
                        new KeyboardButton[]{
                                new KeyboardButton(messageSource.getMessage("bot.digest.button.back", request)),
                        },
                        new KeyboardButton[]{
                                new KeyboardButton(messageSource.getMessage("bot.digest.button.subscriptions-add", request)),
                                new KeyboardButton(messageSource.getMessage("bot.digest.button.subscriptions-delete", request))
                        }
                ));

        return new SendMessageResponse(sendMessage);
    }

    private BotResponse processBack(BotRequest request) {
        chatService.updateStatus(request.chat(), "SUBSCRIPTIONS");

        return BotResponse.noop().then(BotResponse.callback(bot ->
                eventPublisher.publishEvent(new DigestBotUpdate(request))
        ));
    }

    private boolean isBack(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.back", request)
        );
    }

    private boolean isValidRss(BotRequest request) {
        try {
            return rssReader.read(request.message())
                    .findFirst()
                    .isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean supports(BotRequest request) {
        return StringUtils.equalsIgnoreCase(request.chatStatus(), "SUBSCRIPTIONS_ADD");
    }
}
