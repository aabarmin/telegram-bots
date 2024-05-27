package dev.abarmin.bots.repository;

import dev.abarmin.bots.entity.rss.Article;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface ArticleRepository extends ListCrudRepository<Article, Integer> {
    Optional<Article> findByArticleUri(URI articleUri);

    @Query("""
        select article.* from ARTICLES as article
        where article.ARTICLE_SOURCE_ID = :sourceId
        order by article.ARTICLE_ADDED desc
        limit :limit
        """)
    Collection<Article> findAllPublished(
            int sourceId,
            int limit
    );

    @Query("""
            select article.* from ARTICLES as article
            where article.ARTICLE_SOURCE_ID = :sourceId and
                    FORMATDATETIME(article.ARTICLE_ADDED, 'yyy-MM-dd') = :publicationDate
            order by article.ARTICLE_ADDED desc
            limit :limit
            """)
    Collection<Article> findAllPublished(
            int sourceId,
            LocalDate publicationDate,
            int limit
    );
}
