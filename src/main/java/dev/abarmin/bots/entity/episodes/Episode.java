package dev.abarmin.bots.entity.episodes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("EPISODES")
public class Episode {
    @Id
    @Column("EPISODE_ID")
    private Integer id;
    @Column("EPISODE_NAME")
    private String episodeName;
    @Column("EPISODE_STATUS")
    private EpisodeStatus episodeStatus;
    @CreatedDate
    @Builder.Default
    @Column("CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();
    @LastModifiedDate
    @Column("UPDATED_AT")
    private LocalDateTime updatedAt;
}
