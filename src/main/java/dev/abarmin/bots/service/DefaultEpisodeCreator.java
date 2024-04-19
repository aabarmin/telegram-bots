package dev.abarmin.bots.service;

import dev.abarmin.bots.entity.episodes.Episode;
import dev.abarmin.bots.entity.episodes.EpisodeStatus;
import dev.abarmin.bots.repository.EpisodesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultEpisodeCreator implements ApplicationRunner {
    public static final String DEFAULT_EPISODE_NAME = "Not interesting";
    public static final String DEFAULT_OLD_EPISODE_NAME = "Useless";

    private final EpisodesRepository episodesRepository;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Checking if default episode exists");
        if (episodesRepository.findByEpisodeName(DEFAULT_EPISODE_NAME).isPresent()) {
            log.info("Default episode exists, nothing to do");
            return;
        }
        log.info("Default episode does not exist, maybe old version exists");
        if (episodesRepository.findByEpisodeName(DEFAULT_OLD_EPISODE_NAME).isPresent()) {
            log.info("Should rename existing episode");
            episodesRepository.findByEpisodeName(DEFAULT_OLD_EPISODE_NAME)
                    .map(episode -> {
                        episode.setEpisodeName(DEFAULT_EPISODE_NAME);
                        episode.setUpdatedAt(LocalDateTime.now());
                        return episode;
                    })
                    .ifPresent(episodesRepository::save);
            log.info("Old episode renamed");
            return;
        }
        log.info("Creating default episode");
        episodesRepository.save(Episode.builder()
                .episodeName(DEFAULT_EPISODE_NAME)
                .episodeStatus(EpisodeStatus.PUBLISHED)
                .build());
    }
}
