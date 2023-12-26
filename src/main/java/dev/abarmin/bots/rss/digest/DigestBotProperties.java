package dev.abarmin.bots.rss.digest;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("telegram.bot.rss.digest")
public record DigestBotProperties(
        @NotEmpty String botName,
        @NotEmpty String botToken,
        @NotEmpty String webHookUrl,
        @NotEmpty String sqsQueue
) {
}
