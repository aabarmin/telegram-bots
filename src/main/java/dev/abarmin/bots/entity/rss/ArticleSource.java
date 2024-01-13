package dev.abarmin.bots.entity.rss;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.net.URI;
import java.time.LocalDateTime;

@Table("ARTICLE_SOURCES")
public record ArticleSource(
        @Column("SOURCE_ID") @Id Integer id,
        @Column("SOURCE_NAME") String sourceName,
        @Column("SOURCE_URL") URI sourceUri,
        @Column("SOURCE_LAST_UPDATED") LocalDateTime lastUpdated,
        @Column("CREATED_AT") @CreatedDate LocalDateTime createdAt,
        @Column("UPDATED_AT") @LastModifiedDate LocalDateTime updatedAt
) {

    public ArticleSource(String sourceName, URI sourceUri) {
        this(
                null,
                sourceName,
                sourceUri,
                null,
                LocalDateTime.now(),
                null
        );
    }

    public ArticleSource withLastUpdated(LocalDateTime lastUpdated) {
        return new ArticleSource(
                id(),
                sourceName(),
                sourceUri(),
                lastUpdated,
                createdAt(),
                LocalDateTime.now()
        );
    }
}
