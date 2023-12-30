package dev.abarmin.bots.listener;

import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetWebhook;
import dev.abarmin.bots.listener.service.TelegramUpdateService;
import dev.abarmin.bots.rss.digest.DigestBotProperties;
import dev.abarmin.bots.rss.digest.DigestBotUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(
        prefix = "telegram.bot.listener.webhook",
        name = "enabled",
        havingValue = "true"
)
@Slf4j
@RequiredArgsConstructor
public class TelegramBotController implements ApplicationRunner {
    private final DigestBotProperties properties;
    private final TelegramBot bot;
    private final Gson gson;
    private final TelegramUpdateService updateService;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/telegram/rss-digest")
    public void processUpdate(@RequestBody String content) {
        log.debug("Received update {}", content);
        Update update = gson.fromJson(content, Update.class);
        if (!updateService.isProcessed(update)) {
            updateService.save(update);
            publisher.publishEvent(new DigestBotUpdate(update));
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Setting webhook {}", properties.webHook());
        bot.execute(new SetWebhook()
                .url(properties.webHook())
        );
    }
}
