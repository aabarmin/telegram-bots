package dev.abarmin.bots.rss.reader.persistence;

import org.springframework.data.domain.Limit;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
    /**
     * Check if an article with given URL already saved to the DB.
     *
     * @param articleUrl
     * @return
     */
    boolean existsByArticleUrl(String articleUrl);

    Collection<Article> findAllByArticleSourceOrderByArticleAddedDesc(
            AggregateReference<ArticleSource, Integer> reference,
            Limit limit
    );
}
