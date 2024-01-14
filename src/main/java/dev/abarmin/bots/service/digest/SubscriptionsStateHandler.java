package dev.abarmin.bots.service.digest;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.model.DigestBotUpdate;
import dev.abarmin.bots.service.SubscriptionService;
import dev.abarmin.bots.service.TelegramChatService;
import dev.abarmin.bots.service.support.*;
import dev.abarmin.bots.service.support.response.BotResponse;
import dev.abarmin.bots.service.support.response.NoopResponse;
import dev.abarmin.bots.service.support.response.SendMessageResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionsStateHandler implements BotOperation {
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSourceHelper messageSource;
    private final TelegramChatService chatService;
    private final SubscriptionService subscriptionService;

    @Override
    public BotResponse process(BotRequest request) {
        if (isManageSubscriptions(request)) {
            return processManageSubscriptions(request);
        } else if (isBack(request)) {
            return processBack(request);
        } else if (isAddSubscription(request)) {
            return processAddSubscription(request);
        } else if (isDeleteSubscription(request)) {
            return processDeleteSubscription(request);
        }
        return new NoopResponse();
    }

    private BotResponse processDeleteSubscription(BotRequest request) {
        chatService.updateStatus(request.chat(), "SUBSCRIPTIONS_DELETE");
        return BotResponse.noop().then(BotResponse.callback(bot -> {
            eventPublisher.publishEvent(new DigestBotUpdate(request.update()));
        }));
    }

    private boolean isDeleteSubscription(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.subscriptions-delete", request)
        );
    }

    private BotResponse processAddSubscription(BotRequest request) {
        chatService.updateStatus(request.chat(), "SUBSCRIPTIONS_ADD");
        eventPublisher.publishEvent(new DigestBotUpdate(request.update()));
        return new NoopResponse();
    }

    private BotResponse processBack(BotRequest request) {
        chatService.updateStatus(request.chat(), "CREATED");
        var message = new SendMessage(
                request.chatId(),
                messageSource.getMessage("bot.digest.button.subscriptions-back-success", request)
        );
        return BotResponse.message(message).then(BotResponse.callback(bot -> {
            eventPublisher.publishEvent(new DigestBotUpdate(request.update()));
        }));
    }

    private boolean isBack(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.back", request)
        );
    }

    private boolean isManageSubscriptions(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.subscriptions", request)
        );
    }

    private boolean isAddSubscription(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.subscriptions-add", request)
        );
    }

    private BotResponse processManageSubscriptions(BotRequest request) {
        var sources = subscriptionService.findSubscriptions(request.chat())
                .stream()
                .map(source -> String.format("%s", source.sourceName()))
                .collect(Collectors.joining("\n"));

        var message = new SendMessage(
                request.chatId(),
                messageSource.getMessage("bot.digest.button.subscriptions-manage", request)
                        + "\n\n"
                        + sources
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

        return new SendMessageResponse(message);
    }

    @Override
    public boolean supports(BotRequest request) {
        return StringUtils.equalsIgnoreCase(request.chatStatus(), "SUBSCRIPTIONS");
    }
}
