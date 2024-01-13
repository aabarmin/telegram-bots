package dev.abarmin.bots.service.digest.processor;

import com.google.common.collect.Lists;
import dev.abarmin.bots.model.digest.Digest;
import dev.abarmin.bots.model.digest.DigestItem;
import dev.abarmin.bots.model.digest.DigestSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DigestConverter {
    public String toMarkdown(Digest digest, String header) {
        if (digest.isEmpty()) {
            return "Nothing new today";
        }
        var parts = Lists.newArrayList();
        parts.add(header);
        digest.sources()
                .stream()
                .filter(source -> !source.isEmpty())
                .map(this::toMarkdown)
                .forEach(parts::add);
        return StringUtils.join(parts, "\n\n");
    }

    private String toMarkdown(DigestSource source) {
        var parts = Lists.newArrayList();
        parts.add(String.format("*%s*", source.sourceName()));
        int index = 1;
        for (DigestItem item : source.items()) {
            parts.add(String.format(
                    "%s. [%s](%s)",
                    index++,
                    item.itemTitle(),
                    item.itemUrl()
            ));
        }
        return StringUtils.join(parts, "\n");
    }
}
