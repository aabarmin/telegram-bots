package dev.abarmin.bots.service.digest.processor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.service.support.BotHelper;
import dev.abarmin.bots.service.support.MessageSourceHelper;
import dev.abarmin.bots.model.digest.Digest;
import dev.abarmin.bots.service.impl.DigestServiceImpl;
import dev.abarmin.bots.model.digest.DigestItem;
import dev.abarmin.bots.model.digest.DigestSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DigestOperationProcessor {
    private final DigestServiceImpl digestBuilder;
    private final MessageSourceHelper messageSource;
    private final TelegramBot digestBot;
    private final BotHelper helper;
    private final DigestConverter converter;

    public void process(Update update) {
        var digest = converter.toMarkdown(
                digestBuilder.create(helper.getChat(update)),
                messageSource.getMessage("bot.digest.header", update, LocalDate.now())
        );

        digestBot.execute(new SendMessage(
                helper.getChatId(update),
                digest
        ).parseMode(ParseMode.Markdown));
    }
}
