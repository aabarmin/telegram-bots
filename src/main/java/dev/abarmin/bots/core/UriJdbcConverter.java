package dev.abarmin.bots.core;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.net.URI;

public class UriJdbcConverter {
    @WritingConverter
    public static class WriteUriConverter implements Converter<URI, String> {
        @Override
        public String convert(URI source) {
            return source.toString();
        }
    }

    @ReadingConverter
    public static class ReadUriConverter implements Converter<String, URI> {
        @Override
        public URI convert(String source) {
            return URI.create(source);
        }
    }
}
