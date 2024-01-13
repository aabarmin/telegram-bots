package dev.abarmin.bots.model.digest;

import java.util.Collection;

public record Digest(
        Collection<DigestSource> sources
) {
}
