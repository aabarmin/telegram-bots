package dev.abarmin.bots.service.digest;

import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.entity.rss.ArticleSource;
import dev.abarmin.bots.entity.telegram.TelegramBotChat;
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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeleteSubscriptionStateHandler implements BotOperation {
    private final BotHelper helper;
    private final ApplicationEventPublisher eventPublisher;
    private final TelegramChatService chatService;
    private final MessageSourceHelper messageSource;
    private final SubscriptionService subscriptionService;

    @Override
    public BotResponse process(BotRequest request) {
        if (isDeleteSubscription(request)) {
            return processDeleteSubscription(request);
        } else if (isBack(request)) {
            return processBack(request);
        } else if (isValidNumber(request)) {
            return processUnsubscribe(request);
        } else {
            return processInvalidMessage(request);
        }
    }

    private BotResponse processInvalidMessage(BotRequest request) {
        var message = new SendMessage(
                request.chat().chatId(),
                messageSource.getMessage("bot.digest.button.subscriptions-delete-invalid-number", request.update())
        );

        return new SendMessageResponse(message);
    }

    private BotResponse processUnsubscribe(BotRequest request) {
        var subscriptions = subscriptions(request.chat());
        var articleSource = subscriptions.get(request.message());

        subscriptionService.unsubscribe(request.chat(), articleSource);
        chatService.updateStatus(request.chat(), "SUBSCRIPTIONS");

        return BotResponse.message(new SendMessage(
                request.chat().chatId(),
                messageSource.getMessage("bot.digest.button.subscriptions-delete-success", request.update())
        )).then(BotResponse.callback(bot -> {
            eventPublisher.publishEvent(new DigestBotUpdate(
                    helper.withMessage(
                            request.update(),
                            messageSource.getMessage("bot.digest.button.subscriptions", request.update())
                    )
            ));
        }));
    }

    private boolean isValidNumber(BotRequest request) {
        return subscriptions(request.chat())
                .containsKey(request.message());
    }

    private BotResponse processBack(BotRequest request) {
        chatService.updateStatus(request.chat(), "SUBSCRIPTIONS");
        eventPublisher.publishEvent(new DigestBotUpdate(request.update()));

        return new NoopResponse();
    }

    private BotResponse processDeleteSubscription(BotRequest request) {
        var subscriptions = subscriptions(request.chat())
                .entrySet()
                .stream()
                .map(entry -> String.format("%s. %s", entry.getKey(), entry.getValue().sourceName()))
                .collect(Collectors.joining("\n"));

        var message = new SendMessage(
                request.chat().chatId(),
                messageSource.getMessage("bot.digest.button.subscriptions-delete-request", request.update())
                        + "\n\n"
                        + subscriptions
        ).replyMarkup(new ReplyKeyboardRemove());

        return new SendMessageResponse(message);
    }

    private boolean isDeleteSubscription(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.subscriptions-delete", request.update())
        );
    }

    private boolean isBack(BotRequest request) {
        return StringUtils.equalsIgnoreCase(
                request.message(),
                messageSource.getMessage("bot.digest.button.back", request.update())
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
    public boolean supports(BotRequest request) {;
        return StringUtils.equalsIgnoreCase(request.chat().chatStatus(), "SUBSCRIPTIONS_DELETE");
    }
}
