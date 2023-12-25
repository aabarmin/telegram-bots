package dev.abarmin.bots.rssreader.service;

import dev.abarmin.bots.rssreader.persistence.Article;
import dev.abarmin.bots.rssreader.persistence.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository repository;

    @Override
    public boolean isAvailable(Article article) {
        return repository.existsByArticleUrl(article.articleUrl());
    }

    @Override
    public Article save(Article article) {
        return repository.save(article);
    }
}
