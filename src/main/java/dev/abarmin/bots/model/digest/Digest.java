package dev.abarmin.bots.model.digest;

import java.util.Collection;

public record Digest(
        Collection<DigestSource> sources
) {
    public boolean isEmpty() {
        return sources.isEmpty() ||
                sources.stream()
                        .allMatch(DigestSource::isEmpty);
    }
}
