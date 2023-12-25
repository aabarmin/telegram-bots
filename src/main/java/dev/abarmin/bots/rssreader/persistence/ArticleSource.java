package dev.abarmin.bots.rssreader.persistence;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table("ARTICLE_SOURCES")
public record ArticleSource(
        @Column("SOURCE_ID") @Id Integer id,
        @Column("SOURCE_NAME") String sourceName,
        @Column("SOURCE_URL") String sourceUrl,
        @Column("SOURCE_LAST_UPDATED") LocalDateTime lastUpdated,
        @Column("CREATED_AT") @CreatedDate LocalDateTime createdAt,
        @Column("UPDATED_AT") @LastModifiedDate LocalDateTime updatedAt
) {

    public ArticleSource withLastUpdated(LocalDateTime lastUpdated) {
        return new ArticleSource(
                id(),
                sourceName(),
                sourceUrl(),
                lastUpdated,
                createdAt(),
                updatedAt()
        );
    }
}
