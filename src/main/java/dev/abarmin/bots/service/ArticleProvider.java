package dev.abarmin.bots.service;

import dev.abarmin.bots.controller.model.ArticleEpisode;
import dev.abarmin.bots.controller.model.ArticleRow;
import dev.abarmin.bots.model.ArticlesRequest;
import dev.abarmin.bots.model.ArticlesResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static dev.abarmin.bots.entity.jooq.Tables.ARTICLES;
import static dev.abarmin.bots.entity.jooq.Tables.ARTICLE_SOURCES;
import static dev.abarmin.bots.entity.jooq.Tables.EPISODES;
import static dev.abarmin.bots.entity.jooq.Tables.EPISODES_ARTICLES;

@Component
@RequiredArgsConstructor
public class ArticleProvider {
    public static final int PAGES_PER_PAGE = 50;

    private final DSLContext dslContext;

    public ArticlesResponse getArticles(@NonNull ArticlesRequest request) {
        Collection<Field<?>> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(ARTICLES.fields()));
        fields.addAll(Arrays.asList(ARTICLE_SOURCES.fields()));

        var builder = dslContext.select(fields)
                .from(ARTICLES)
                .innerJoin(ARTICLE_SOURCES).on(ARTICLES.ARTICLE_SOURCE_ID.eq(ARTICLE_SOURCES.SOURCE_ID));

        Collection<Condition> conditions = new ArrayList<>();
        if (!request.isShowAll()) {
            builder = builder
                    .leftJoin(EPISODES_ARTICLES)
                    .on(EPISODES_ARTICLES.ARTICLE_ID.eq(ARTICLES.ARTICLE_ID));
            conditions.add(EPISODES_ARTICLES.EPISODE_ID.isNull());
        }
        if (!request.getSources().isEmpty()) {
            conditions.add(ARTICLE_SOURCES.SOURCE_ID.in(request.getSources()));
        } else {
            conditions.add(DSL.trueCondition());
        }

        // need to know how many records available
        int numberOfRecords = builder.where(DSL.and(conditions)).fetch().size();

        // select only a page of records
        List<ArticleRow> records = builder.where(DSL.and(conditions))
                .orderBy(ARTICLES.ARTICLE_ADDED.desc())
                .limit(PAGES_PER_PAGE * request.getPage(), PAGES_PER_PAGE)
                .fetch(this::toRecord);


        return ArticlesResponse.builder()
                .articles(records)
                .page(request.getPage())
                .totalPages(numberOfRecords / PAGES_PER_PAGE + 1)
                .build();
    }

    private ArticleRow toRecord(Record record) {
        return ArticleRow.builder()
                .articleId(record.get(ARTICLES.ARTICLE_ID))
                .articleTitle(record.get(ARTICLES.ARTICLE_TITLE))
                .articleUrl(record.get(ARTICLES.ARTICLE_URL))
                .articleAdded(record.get(ARTICLES.ARTICLE_ADDED))
                .sourceId(record.get(ARTICLE_SOURCES.SOURCE_ID))
                .sourceName(record.get(ARTICLE_SOURCES.SOURCE_NAME))
                .episodes(getEpisodes(record.get(ARTICLES.ARTICLE_ID)))
                .build();
    }

    private Collection<ArticleEpisode> getEpisodes(int articleId) {
        return dslContext.select(EPISODES.EPISODE_ID, EPISODES.EPISODE_NAME)
                .from(EPISODES)
                .innerJoin(EPISODES_ARTICLES).on(EPISODES_ARTICLES.EPISODE_ID.eq(EPISODES.EPISODE_ID))
                .innerJoin(ARTICLES).on(EPISODES_ARTICLES.ARTICLE_ID.eq(ARTICLES.ARTICLE_ID))
                .where(ARTICLES.ARTICLE_ID.eq(articleId))
                .fetch()
                .map(record -> {
                    return ArticleEpisode.builder()
                            .episodeId(record.get(EPISODES.EPISODE_ID))
                            .episodeName(record.get(EPISODES.EPISODE_NAME))
                            .build();
                });
    }
}