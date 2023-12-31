package dev.abarmin.bots.rss.reader.service;

import dev.abarmin.bots.rss.reader.persistence.Article;
import dev.abarmin.bots.rss.reader.persistence.ArticleRepository;
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
