package dev.abarmin.bots.rssreader;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collection;

@ConfigurationProperties(prefix = "telegram.bot.rss.reader")
public record RssReaderProperties(
        Collection<RssReaderSource> sources
) {}
