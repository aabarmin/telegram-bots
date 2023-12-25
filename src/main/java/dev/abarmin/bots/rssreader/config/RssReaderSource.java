package dev.abarmin.bots.rssreader.config;

import lombok.Value;

public record RssReaderSource(
        String sourceName,
        String sourceUrl
) {}
