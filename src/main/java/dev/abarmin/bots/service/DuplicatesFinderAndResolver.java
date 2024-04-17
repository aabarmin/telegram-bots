package dev.abarmin.bots.service;

import dev.abarmin.bots.entity.episodes.Episode;
import dev.abarmin.bots.entity.episodes.EpisodeStatus;
import dev.abarmin.bots.entity.rss.Article;
import dev.abarmin.bots.repository.EpisodesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static dev.abarmin.bots.entity.jooq.Tables.ARTICLES;
import static dev.abarmin.bots.entity.jooq.Tables.EPISODES_ARTICLES;

@Slf4j
@Component
@RequiredArgsConstructor
public class DuplicatesFinderAndResolver {
    private static final String DUPLICATES_EPISODE_NAME = "Duplicates";

    private final EpisodesRepository episodesRepository;
    private final DSLContext dslContext;

    public void findAndResolveDuplicates() {
        checkDuplicatesEpisodeExists();
        processDuplicates();
    }

    private void checkDuplicatesEpisodeExists() {
        if (episodesRepository.findByEpisodeName(DUPLICATES_EPISODE_NAME).isEmpty()) {
            log.info("Duplicates episode not found. Creating new one.");
            episodesRepository.save(Episode.builder()
                    .episodeName(DUPLICATES_EPISODE_NAME)
                    .episodeStatus(EpisodeStatus.PUBLISHED)
                    .build());
        }
    }

    private void processDuplicates() {
        Episode duplicatesEpisode = episodesRepository.findByEpisodeName(DUPLICATES_EPISODE_NAME).orElseThrow();

        final List<String> candidateTitles = dslContext.select(ARTICLES.ARTICLE_TITLE)
                .from(ARTICLES)
                .leftJoin(EPISODES_ARTICLES).on(EPISODES_ARTICLES.ARTICLE_ID.eq(ARTICLES.ARTICLE_ID))
                .where(EPISODES_ARTICLES.EPISODE_ID.isNull())
                .groupBy(ARTICLES.ARTICLE_TITLE)
                .having(DSL.count(ARTICLES.ARTICLE_TITLE).gt(1))
                .fetch(ARTICLES.ARTICLE_TITLE);
        log.info("Found [{}] candidates for deduplication", candidateTitles.size());

        for (String candidateTitle : candidateTitles) {
            List<Integer> foundIds = dslContext.select(ARTICLES.ARTICLE_ID)
                    .from(ARTICLES)
                    .where(ARTICLES.ARTICLE_TITLE.eq(candidateTitle))
                    .fetch(ARTICLES.ARTICLE_ID);
            if (foundIds.size() == 1) {
                log.info("Skipping article with name [{}] as it was incorrectly identified", candidateTitle);
                continue;
            }
            log.info("Found [{}] duplicates for article with name [{}]", foundIds.size(), candidateTitle);
            List<Integer> duplicates = foundIds.subList(1, foundIds.size());
            for (Integer duplicateId : duplicates) {
                dslContext.insertInto(EPISODES_ARTICLES)
                        .set(EPISODES_ARTICLES.EPISODE_ID, duplicatesEpisode.getId())
                        .set(EPISODES_ARTICLES.ARTICLE_ID, duplicateId)
                        .execute();
            }
        }
        log.info("Done");
    }
}
