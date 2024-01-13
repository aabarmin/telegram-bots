package dev.abarmin.bots.model.digest;

import java.util.Collection;

public record DigestSource(
        String sourceName,
        Collection<DigestItem> items
) {
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
