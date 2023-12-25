package dev.abarmin.bots.rss.reader.persistence;

import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
    boolean existsByArticleUrl(String articleUrl);
}
