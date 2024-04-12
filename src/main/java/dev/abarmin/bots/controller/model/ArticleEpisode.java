package dev.abarmin.bots.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleEpisode {
    private int episodeId;
    private String episodeName;
}
