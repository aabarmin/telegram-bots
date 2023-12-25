package dev.abarmin.bots.rssreader.service;

import dev.abarmin.bots.rssreader.persistence.ArticleSource;
import dev.abarmin.bots.rssreader.persistence.ArticleSourceRepository;
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
