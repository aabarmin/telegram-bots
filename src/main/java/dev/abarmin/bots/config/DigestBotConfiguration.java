package dev.abarmin.bots.config;

import com.pengrad.telegrambot.TelegramBot;
import dev.abarmin.bots.rss.digest.DigestBotProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@RequiredArgsConstructor
public class DigestBotConfiguration {
    private final DigestBotProperties properties;

    @Bean
    public TelegramBot digestBot() {
        return new TelegramBot(properties.botToken());
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:/messages"
        );
        return messageSource;
    }
}
