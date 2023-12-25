package dev.abarmin.bots.rssreader.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collection;

@ConfigurationProperties(prefix = "telegram.bot.rss.reader")
public record RssReaderProperties(
        @NotEmpty Collection<RssReaderSource> sources,
        @NotNull RssReaderSchedulerProperties scheduler
) {}
