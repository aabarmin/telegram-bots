package dev.abarmin.bots.rss.reader.digest;

import java.util.Collection;

public record DigestSource(
        String sourceName,
        Collection<DigestItem> items
) {
}
