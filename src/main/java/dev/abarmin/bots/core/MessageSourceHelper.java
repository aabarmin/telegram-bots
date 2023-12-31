package dev.abarmin.bots.core;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageSourceHelper {
    private final MessageSource messageSource;

    public String getMessage(String template, Update update, Object... params) {
        return messageSource.getMessage(
                template,
                params,
                Locale.forLanguageTag(Optional.of(update)
                        .map(Update::message)
                        .map(Message::from)
                        .map(User::languageCode)
                        .orElse("en"))
        );
    }
}
