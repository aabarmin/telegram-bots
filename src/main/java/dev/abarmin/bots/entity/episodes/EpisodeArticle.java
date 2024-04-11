package dev.abarmin.bots.entity.episodes;

import dev.abarmin.bots.entity.rss.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("EPISODES_ARTICLES")
public class EpisodeArticle {
    @Id
    @Column("ID")
    private Integer id;
    @Column("EPISODE_ID")
    private AggregateReference<Episode, Integer> episodeId;
    @Column("ARTICLE_ID")
    private AggregateReference<Article, Integer> articleId;
}
