package dev.abarmin.bots.model;

import dev.abarmin.bots.controller.model.ArticleRow;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class ArticlesResponse {
    private Collection<ArticleRow> articles;
    private int page;
    private int totalPages;
}
