package dev.abarmin.bots.rssreader;

import lombok.Value;

public record RssReaderSource(
        String sourceName,
        String sourceUrl
) {}
