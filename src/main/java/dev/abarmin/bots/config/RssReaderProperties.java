package dev.abarmin.bots.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram.bot.rss.reader")
public record RssReaderProperties(
        @NotNull RssReaderSchedulerProperties scheduler
) {}
