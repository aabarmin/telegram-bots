package dev.abarmin.bots.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.model.DigestBotUpdate;
import dev.abarmin.bots.service.support.*;
import dev.abarmin.bots.service.support.response.BotResponse;
import dev.abarmin.bots.service.support.response.SendMessageResponse;
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
        botResponse.send(digestBot);
    }

    private BotResponse notSupported(BotRequest request) {
        return new SendMessageResponse(new SendMessage(
                request.chat().chatId(),
                messageSource.getMessage(
                        "bot.digest.not-supported",
                        request.update()
                )
        ));
    }
}
