package dev.abarmin.bots.service.digest.processor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.service.DigestService;
import dev.abarmin.bots.service.support.BotHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class WhatsNewOperationProcessor {
    private final TelegramBot telegramBot;
    private final DigestService digestService;
    private final BotHelper helper;
    private final DigestConverter converter;

    public void process(Update update) {
        var digest = digestService.create(
                helper.getChat(update),
                LocalDate.now()
        );

        telegramBot.execute(new SendMessage(
                helper.getChatId(update),
                converter.toMarkdown(digest, "What is new today")
        ).parseMode(ParseMode.Markdown));
    }
}
