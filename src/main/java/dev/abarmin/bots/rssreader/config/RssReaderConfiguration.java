package dev.abarmin.bots.rssreader.config;

import com.apptasticsoftware.rssreader.RssReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RssReaderConfiguration {
    @Bean
    public RssReader rssReader() {
        return new RssReader();
    }
}
