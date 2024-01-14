package dev.abarmin.bots.service.support;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import dev.abarmin.bots.model.request.BotRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageSourceHelper {
    private final MessageSource messageSource;

    public String getMessage(String template, BotRequest request, Object... params) {
        return getMessage(template, request.update(), params);
    }

    private String getMessage(String template, Update update, Object... params) {
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
