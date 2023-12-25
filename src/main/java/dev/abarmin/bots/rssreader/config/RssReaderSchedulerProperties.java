package dev.abarmin.bots.rssreader.config;

import jakarta.validation.constraints.NotNull;

import java.time.Duration;

public record RssReaderSchedulerProperties(
        @NotNull Duration initialDelay,
        @NotNull Duration fixedDelay
) {
}
