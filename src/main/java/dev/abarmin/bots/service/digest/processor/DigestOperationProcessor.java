package dev.abarmin.bots.service.digest.processor;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.service.impl.DigestServiceImpl;
import dev.abarmin.bots.model.request.BotRequest;
import dev.abarmin.bots.model.response.BotResponse;
import dev.abarmin.bots.service.support.MessageSourceHelper;
import dev.abarmin.bots.model.response.SendMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DigestOperationProcessor {
    private final DigestServiceImpl digestBuilder;
    private final MessageSourceHelper messageSource;
    private final DigestConverter converter;

    public BotResponse process(BotRequest request) {
        var digest = converter.toMarkdown(
                digestBuilder.create(request.chat()),
                messageSource.getMessage("bot.digest.header.last-5", request, LocalDate.now()),
                messageSource.getMessage("bot.digest.no-updates", request)
        );

        var message = new SendMessage(
                request.chatId(),
                digest
        ).parseMode(ParseMode.Markdown);

        return new SendMessageResponse(message);
    }
}
