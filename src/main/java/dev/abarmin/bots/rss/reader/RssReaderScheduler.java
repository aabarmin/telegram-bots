package dev.abarmin.bots.rss.reader;

import dev.abarmin.bots.rss.reader.service.ArticleReader;
import dev.abarmin.bots.rss.reader.service.ArticleService;
import dev.abarmin.bots.rss.reader.service.ArticleSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class RssReaderScheduler {
    private final ArticleSourceService sourceService;
    private final ArticleReader articleReader;
    private final ArticleService articleService;

    @Scheduled(
            initialDelayString = "${telegram.bot.rss.reader.scheduler.initialDelay}",
            fixedDelayString = "${telegram.bot.rss.reader.scheduler.fixedDelay}"
    )
    public void update() {
        sourceService.findAllSources()
                .stream()
                .map(articleReader::read)
                .flatMap(Collection::stream)
                .filter(Predicate.not(articleService::isAvailable))
                .forEach(articleService::save);
    }
}