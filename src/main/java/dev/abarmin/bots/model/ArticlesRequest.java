package dev.abarmin.bots.model;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class ArticlesRequest {
    private boolean showAll;
    private Collection<Integer> sources;
    private int page;
}
