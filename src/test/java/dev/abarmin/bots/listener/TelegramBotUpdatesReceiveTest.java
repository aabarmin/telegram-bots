package dev.abarmin.bots.listener;

import dev.abarmin.bots.BotsApplication;
import dev.abarmin.bots.controller.TelegramBotController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.*;

class TelegramBotUpdatesReceiveTest {
    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(BotsApplication.class))
            .withPropertyValues(
                    "spring.liquibase.enabled=false",
                    "telegram.bot.rss.digest.scheduler.enabled=false",
                    "telegram.bot.rss.reader.scheduler.initialDelay=PT5S",
                    "telegram.bot.rss.reader.scheduler.fixedDelay=PT1H"
            );

    @Test
    void polling_noController() {
        runner.withPropertyValues(
                "telegram.bot.listener.webhook.enabled=false",
                "telegram.bot.listener.polling.enabled=true"
        ).run(context -> {
            assertThat(context).hasSingleBean(TelegramBotListener.class);
            assertThat(context).doesNotHaveBean(TelegramBotController.class);
        });
    }

    @Test
    void push_noListener() {
        runner.withPropertyValues(
                "telegram.bot.listener.webhook.enabled=true",
                "telegram.bot.listener.polling.enabled=false"
        ).run(context -> {
            assertThat(context).hasSingleBean(TelegramBotController.class);
            assertThat(context).doesNotHaveBean(TelegramBotListener.class);
        });
    }
}