package dev.abarmin.bots.rss.digest;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("telegram.bot.rss.digest")
public record DigestBotProperties(
        String botName,
        String botToken
) {
}
