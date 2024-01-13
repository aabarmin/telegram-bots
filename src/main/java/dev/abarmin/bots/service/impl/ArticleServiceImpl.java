package dev.abarmin.bots.service.impl;

import dev.abarmin.bots.entity.Article;
import dev.abarmin.bots.repository.ArticleRepository;
import dev.abarmin.bots.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository repository;

    @Override
    public Article save(Article article) {
        return repository.findByArticleUri(article.articleUri())
                .orElseGet(() -> repository.save(article));
    }
}
