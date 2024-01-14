package dev.abarmin.bots.service.digest.processor;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.service.DigestService;
import dev.abarmin.bots.service.support.BotRequest;
import dev.abarmin.bots.service.support.response.BotResponse;
import dev.abarmin.bots.service.support.MessageSourceHelper;
import dev.abarmin.bots.service.support.response.SendMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class WhatsNewOperationProcessor {
    private final DigestService digestService;
    private final DigestConverter converter;
    private final MessageSourceHelper messageSource;

    public BotResponse process(BotRequest request) {
        var digest = digestService.create(
                request.chat(),
                LocalDate.now()
        );

        var message = new SendMessage(
                request.chat().chatId(),
                converter.toMarkdown(
                        digest,
                        messageSource.getMessage("bot.digest.header.whats-new-today", request.update()),
                        messageSource.getMessage("bot.digest.no-updates", request.update())
                )
        ).parseMode(ParseMode.Markdown);

        return new SendMessageResponse(message);
    }
}
