package dev.abarmin.bots.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.model.request.BotRequest;
import dev.abarmin.bots.model.DigestBotUpdate;
import dev.abarmin.bots.model.response.CallbackResponse;
import dev.abarmin.bots.model.response.NoopResponse;
import dev.abarmin.bots.service.support.*;
import dev.abarmin.bots.model.response.BotResponse;
import dev.abarmin.bots.model.response.SendMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "telegram.bot.rss.digest",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class DigestBotEventListener {
    private final TelegramBot digestBot;
    private final BotHelper helper;
    private final MessageSourceHelper messageSource;
    private final Collection<BotOperation> operations;

    @EventListener(DigestBotUpdate.class)
    public void onEvent(DigestBotUpdate update) {
        var botRequest = helper.toRequest(update.update());
        var botResponse = operations.stream()
                .filter(op -> op.supports(botRequest))
                .findFirst()
                .orElseGet(() -> this::notSupported)
                .process(botRequest);
        sendMessage(botResponse, digestBot);
    }

    private void sendMessage(BotResponse<?> botResponse, TelegramBot digestBot) {
        if (botResponse instanceof SendMessageResponse sendMessage) {
            digestBot.execute(sendMessage.message());
        } else if (botResponse instanceof NoopResponse noop) {
            // do nothing
        } else if (botResponse instanceof CallbackResponse callback) {
            callback.callback().accept(digestBot);
        } else {
            throw new UnsupportedOperationException();
        }
        botResponse.nextActions().forEach(action -> sendMessage(action, digestBot));
    }

    private SendMessageResponse notSupported(BotRequest request) {
        return new SendMessageResponse(new SendMessage(
                request.chatId(),
                messageSource.getMessage(
                        "bot.digest.not-supported",
                        request
                )
        ));
    }
}
