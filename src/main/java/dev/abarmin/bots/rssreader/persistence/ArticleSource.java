package dev.abarmin.bots.rssreader.persistence;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("ARTICLE_SOURCES")
public record ArticleSource(
        @Column("ID") @Id Integer id,
        @Column("SOURCE_NAME") String sourceName,
        @Column("SOURCE_URL") String sourceUrl,
        @Column("SOURCE_LAST_UPDATED") LocalDate lastUpdated,
        @Column("CREATED_AT") @CreatedDate LocalDate createdAt,
        @Column("UPDATED_AT") @LastModifiedDate LocalDate updatedAt
        ) {
}
