package dev.abarmin.bots.rss.digest;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DigestBotConfiguration {
    private final DigestBotProperties properties;

    @Bean
    public TelegramBot digestBot() {
        return new TelegramBot(properties.botToken());
    }
}
