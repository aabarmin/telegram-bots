package dev.abarmin.bots.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.DeleteWebhook;
import dev.abarmin.bots.service.TelegramUpdateService;
import dev.abarmin.bots.rss.digest.DigestBotUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "telegram.bot.listener.polling",
        name = "enabled",
        havingValue = "true"
)
public class TelegramBotListener implements ApplicationRunner {
    private final ApplicationEventPublisher publisher;
    private final TelegramUpdateService updateService;
    private final TelegramBot digestBot;

    @Override
    public void run(ApplicationArguments args) {
        digestBot.setUpdatesListener(updates -> {
            updates.stream()
                    .filter(Predicate.not(updateService::isProcessed))
                    .map(updateService::save)
                    .map(DigestBotUpdate::new)
                    .forEach(publisher::publishEvent);

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        digestBot.execute(new DeleteWebhook());
    }
}