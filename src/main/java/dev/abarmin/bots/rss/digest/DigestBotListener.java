package dev.abarmin.bots.rss.digest;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.core.MessageSourceHelper;
import dev.abarmin.bots.rss.reader.digest.Digest;
import dev.abarmin.bots.rss.reader.digest.DigestBuilder;
import dev.abarmin.bots.rss.reader.digest.DigestItem;
import dev.abarmin.bots.rss.reader.digest.DigestSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DigestBotListener {
    private final TelegramBot digestBot;
    private final DigestBuilder digestBuilder;
    private final MessageSourceHelper messageSource;

    @EventListener(DigestBotUpdate.class)
    public void onEvent(DigestBotUpdate update) {
        final Update telegramUpdate = update.update();
        final String message = telegramUpdate.message().text();

        switch (message) {
            case "/digest" -> digest(telegramUpdate);
            default -> notSupported(telegramUpdate);
        }
    }

    private void digest(Update update) {
        StringBuilder builder = new StringBuilder();
        builder.append(messageSource.getMessage(
                "bot.digest.header",
                LocalDate.now()
        ));

        Digest digest = digestBuilder.create();
        for (DigestSource source : digest.sources()) {
            builder.append(String.format(
                    "\n*%s*\n",
                    source.sourceName()
            ));

            int index = 1;
            for (DigestItem item : source.items()) {
                builder.append(String.format(
                        "%s. [%s](%s)\n",
                        index,
                        item.itemTitle(),
                        item.itemUrl()
                ));
                index++;
            }
        }

        digestBot.execute(new SendMessage(
                update.message().chat().id(),
                builder.toString()
        ).parseMode(ParseMode.Markdown));
    }

    private void notSupported(Update update) {
        digestBot.execute(new SendMessage(
                update.message().chat().id(),
                messageSource.getMessage("bot.digest.not-supported")
        ));
    }
}
