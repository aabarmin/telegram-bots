package dev.abarmin.bots.rss.reader.digest;

import java.util.Collection;

public record Digest(
        Collection<DigestSource> sources
) {
}
