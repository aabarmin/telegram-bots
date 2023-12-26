package dev.abarmin.bots.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetWebhook;
import dev.abarmin.bots.listener.service.TelegramUpdateService;
import dev.abarmin.bots.rss.digest.DigestBotProperties;
import dev.abarmin.bots.rss.digest.DigestBotUpdate;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramSqsQueueListener implements ApplicationRunner {
    private final TelegramBot telegramBot;
    private final DigestBotProperties properties;
    private final ApplicationEventPublisher publisher;
    private final TelegramUpdateService updateService;
    private final Gson gson;

    @SqsListener("${telegram.bot.rss.digest.sqsQueue}")
    public void listen(String updateJson) throws Exception {
        Update update = gson.fromJson(updateJson, Update.class);
        Optional.of(update)
                .filter(Predicate.not(updateService::isProcessed))
                .map(updateService::save)
                .map(DigestBotUpdate::new)
                .ifPresent(publisher::publishEvent);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        telegramBot.execute(new SetWebhook()
                .url(properties.webHookUrl())
        );
    }
}
