package dev.abarmin.bots.rss.reader.service;

import com.apptasticsoftware.rssreader.Channel;
import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import dev.abarmin.bots.rss.reader.persistence.ArticleSource;
import dev.abarmin.bots.rss.reader.persistence.ArticleSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ArticleSourceServiceImpl implements ArticleSourceService {
    private final ArticleSourceRepository repository;
    private final RssReader rssReader;

    @Override
    public Collection<ArticleSource> findAllSources() {
        return repository.findAll();
    }

    @Override
    public ArticleSource findOrCreate(URI rssUri) {
        return repository.findBySourceUri(rssUri)
                .orElseGet(() -> repository.save(new ArticleSource(
                        getRssSourceName(rssUri),
                        rssUri
                )));
    }

    @SneakyThrows
    private String getRssSourceName(URI rssUri) {
        return rssReader.read(rssUri.toString())
                .findFirst()
                .map(Item::getChannel)
                .map(Channel::getTitle)
                .orElse("Title not provided");
    }
}
