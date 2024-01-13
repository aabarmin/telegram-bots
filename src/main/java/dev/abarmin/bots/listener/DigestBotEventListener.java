package dev.abarmin.bots.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.core.BotHelper;
import dev.abarmin.bots.core.BotOperation;
import dev.abarmin.bots.core.MessageSourceHelper;
import dev.abarmin.bots.rss.digest.DigestBotUpdate;
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
        var telegramUpdate = update.update();
        var operation = operations.stream()
                .filter(op -> op.supports(telegramUpdate))
                .findFirst()
                .orElseGet(() -> this::notSupported);
        operation.process(telegramUpdate);
    }

    private void notSupported(Update update) {
        digestBot.execute(new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage(
                        "bot.digest.not-supported",
                        update
                )
        ));
    }
}
