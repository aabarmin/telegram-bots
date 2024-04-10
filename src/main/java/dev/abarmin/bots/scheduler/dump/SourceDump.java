package dev.abarmin.bots.scheduler.dump;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceDump {
    private String name;
    private String url;
    private Collection<ArticleDump> articles;
}
