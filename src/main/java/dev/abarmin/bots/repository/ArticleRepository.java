package dev.abarmin.bots.repository;

import dev.abarmin.bots.rss.reader.persistence.Article;
import dev.abarmin.bots.rss.reader.persistence.ArticleSource;
import org.springframework.data.domain.Limit;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.repository.CrudRepository;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
    Optional<Article> findByArticleUri(URI articleUri);

    Collection<Article> findAllByArticleSourceOrderByArticleAddedDesc(
            AggregateReference<ArticleSource, Integer> reference,
            Limit limit
    );
}
