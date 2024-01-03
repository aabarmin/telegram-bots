package dev.abarmin.bots.rss.reader.persistence;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.net.URI;
import java.time.LocalDateTime;

@Table("ARTICLES")
public record Article(
        @Column("ARTICLE_ID") @Id Integer id,
        @Column("ARTICLE_SOURCE_ID") AggregateReference<ArticleSource, Integer> articleSource,
        @Column("ARTICLE_TITLE") String articleTitle,
        @Column("ARTICLE_URL") URI articleUri,
        @Column("ARTICLE_ADDED") LocalDateTime articleAdded,
        @Column("CREATED_AT") @CreatedDate LocalDateTime createdAt,
        @Column("UPDATED_AT") @LastModifiedDate LocalDateTime updatedAt
        ) {
}
