package dev.abarmin.bots.rss.reader.digest;

import dev.abarmin.bots.rss.reader.persistence.ArticleRepository;
import dev.abarmin.bots.rss.reader.persistence.ArticleSource;
import dev.abarmin.bots.rss.reader.persistence.ArticleSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DigestBuilder {
    private final ArticleSourceRepository sourceRepository;
    private final ArticleRepository articleRepository;

    public Digest create() {
        Collection<DigestSource> sources = sourceRepository.findAllByOrderBySourceNameAsc()
                .stream()
                .map(this::toDigestSource)
                .collect(Collectors.toList());

        return new Digest(sources);
    }

    private DigestSource toDigestSource(ArticleSource source) {
        AggregateReference<ArticleSource, Integer> reference = AggregateReference.to(source.id());
        Collection<DigestItem> articles = articleRepository
                .findAllByArticleSourceOrderByArticleAddedDesc(reference, Limit.of(5))
                .stream()
                .map(article -> new DigestItem(
                        article.articleTitle(),
                        article.articleUrl()
                ))
                .collect(Collectors.toList());

        return new DigestSource(
                source.sourceName(),
                articles
        );
    }
}
