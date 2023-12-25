package dev.abarmin.bots.rss.reader.service;

import dev.abarmin.bots.rss.reader.persistence.ArticleSource;
import dev.abarmin.bots.rss.reader.persistence.ArticleSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ArticleSourceServiceImpl implements ArticleSourceService {
    private final ArticleSourceRepository repository;

    @Override
    public Collection<ArticleSource> findAllSources() {
        return repository.findAll();
    }
}
