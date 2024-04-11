package dev.abarmin.bots.service.digest;

import dev.abarmin.bots.entity.rss.ArticleSource;
import dev.abarmin.bots.repository.ArticleSourceRepository;
import dev.abarmin.bots.scheduler.RssReaderScheduler;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;

@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class DefaultLocalSourcesAdder implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(DefaultLocalSourcesAdder.class);
    private final ArticleSourceRepository sourceRepository;
    private final RssReaderScheduler scheduler;

    private final Map<String, URI> defaultSources = Map.of(
            "Thorben Janssen", URI.create("https://thorben-janssen.com/feed/")
    );

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean hasNew = false;
        for (Map.Entry<String, URI> entry : defaultSources.entrySet()) {
            if (sourceRepository.findBySourceUri(entry.getValue()).isEmpty()) {
                hasNew = true;
                sourceRepository.save(new ArticleSource(entry.getKey(), entry.getValue()));
            }
        }
        if (hasNew) {
            log.info("Loading articles from newly added sources");
            scheduler.update();
        }
    }
}
