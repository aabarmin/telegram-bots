package dev.abarmin.bots.rssreader;

import com.apptasticsoftware.rssreader.RssReader;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class RssReaderConfiguration {
    @Bean
    public RssReader rssReader() {
        return new RssReader();
    }
}
