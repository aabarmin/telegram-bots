package dev.abarmin.bots.scheduler.dump;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataDump {
    @Singular
    private Collection<SourceDump> sources;
}
