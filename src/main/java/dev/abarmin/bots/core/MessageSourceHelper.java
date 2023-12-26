package dev.abarmin.bots.core;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageSourceHelper {
    private final MessageSource messageSource;

    public String getMessage(String template, Object... params) {
        return messageSource.getMessage(
                template,
                params,
                Locale.forLanguageTag("ru-RU")
        );
    }
}
