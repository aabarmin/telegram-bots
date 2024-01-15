package dev.abarmin.bots.service.digest.processor;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.service.DigestService;
import dev.abarmin.bots.model.request.BotRequest;
import dev.abarmin.bots.model.response.BotResponse;
import dev.abarmin.bots.service.support.MessageSourceHelper;
import dev.abarmin.bots.model.response.SendMessageResponse;
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
                request.chatId(),
                converter.toMarkdown(
                        digest,
                        messageSource.getMessage("bot.digest.header.whats-new-today", request),
                        messageSource.getMessage("bot.digest.no-updates", request)
                )
        ).parseMode(ParseMode.Markdown);

        return new SendMessageResponse(message);
    }
}
