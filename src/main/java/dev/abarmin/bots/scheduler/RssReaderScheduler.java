package dev.abarmin.bots.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.abarmin.bots.entity.rss.Article;
import dev.abarmin.bots.entity.rss.ArticleSource;
import dev.abarmin.bots.repository.ArticleRepository;
import dev.abarmin.bots.scheduler.dump.ArticleDump;
import dev.abarmin.bots.scheduler.dump.DataDump;
import dev.abarmin.bots.scheduler.dump.SourceDump;
import dev.abarmin.bots.service.ArticleReaderService;
import dev.abarmin.bots.service.ArticleService;
import dev.abarmin.bots.service.ArticleSourceService;
import groovy.util.logging.Slf4j;
import jdk.jfr.StackTrace;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "telegram.bot.rss.scheduler",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class RssReaderScheduler {
    private final ArticleSourceService sourceService;
    private final ArticleReaderService articleReader;
    private final ArticleService articleService;

    private final ArticleRepository articleRepository;
    private final ObjectMapper objectMapper;

    @Value("${telegram.bot.dump.location}")
    private String dumpLocation;

    @Scheduled(
            initialDelayString = "${telegram.bot.rss.reader.scheduler.initialDelay}",
            fixedDelayString = "${telegram.bot.rss.reader.scheduler.fixedDelay}"
    )
    public void update() throws Exception {
        sourceService.findAllSources()
                .stream()
                .map(articleReader::read)
                .flatMap(Collection::stream)
                .forEach(articleService::save);

        // need to generate json for data export
        List<SourceDump> sourcesDump = sourceService.findAllSources()
                .stream()
                .map(this::dump)
                .toList();
        DataDump dataDump = DataDump.builder()
                .sources(sourcesDump)
                .build();

        Path dumpPath = Path.of(this.dumpLocation);
        Files.deleteIfExists(dumpPath);
        Files.createFile(dumpPath);

        try (OutputStream outputStream = Files.newOutputStream(dumpPath, StandardOpenOption.CREATE)) {
            objectMapper.writeValue(outputStream, dataDump);
        }
    }

    private SourceDump dump(ArticleSource source) {
        List<ArticleDump> articles = articleRepository.findAllByArticleSource(source)
                .stream()
                .map(this::dump)
                .toList();

        return SourceDump.builder()
                .name(source.sourceName())
                .url(source.sourceUri().toString())
                .articles(articles)
                .build();
    }

    private ArticleDump dump(Article article) {
        return ArticleDump.builder()
                .title(article.articleTitle())
                .url(article.articleUri().toString())
                .added(article.articleAdded())
                .build();
    }
}
