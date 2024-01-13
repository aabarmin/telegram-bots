package dev.abarmin.bots.repository;

import dev.abarmin.bots.entity.rss.ArticleSource;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

public interface ArticleSourceRepository extends ListCrudRepository<ArticleSource, Integer> {
    /**
     * Find one by source URI.
     *
     * @param url
     * @return
     */
    Optional<ArticleSource> findBySourceUri(URI url);

    /**
     * Find all sources with the most number of subscribers.
     * @return
     */
    @Query("""
            select source.* from ARTICLE_SOURCES as source
            inner join ARTICLE_SUBSCRIPTIONS as subscription on source.source_id = subscription.article_source_id
            group by source.source_id
            order by count(subscription.id) desc
            """)
    Collection<ArticleSource> findPopularSources();
}
