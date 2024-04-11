package dev.abarmin.bots.controller.model;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class ArticleRow {
    private int articleId;
    private String articleTitle;

    private int sourceId;
    private String sourceName;

    private Collection<ArticleEpisode> episodes;
}
