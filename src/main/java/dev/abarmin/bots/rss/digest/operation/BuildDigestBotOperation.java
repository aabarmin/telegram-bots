package dev.abarmin.bots.rss.digest.operation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.core.BotOperation;
import dev.abarmin.bots.core.MessageSourceHelper;
import dev.abarmin.bots.listener.service.TelegramChatService;
import dev.abarmin.bots.rss.reader.digest.Digest;
import dev.abarmin.bots.rss.reader.digest.DigestBuilder;
import dev.abarmin.bots.rss.reader.digest.DigestItem;
import dev.abarmin.bots.rss.reader.digest.DigestSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class BuildDigestBotOperation implements BotOperation {
    private final TelegramChatService chatService;
    private final DigestBuilder digestBuilder;
    private final MessageSourceHelper messageSource;
    private final TelegramBot digestBot;

    @Override
    public void process(Update update) {
        StringBuilder builder = new StringBuilder();
        builder.append(messageSource.getMessage(
                "bot.digest.header",
                update,
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
                getChatId(update),
                builder.toString()
        ).parseMode(ParseMode.Markdown));
    }

    @Override
    public boolean supports(Update update) {
        var botChat = chatService.findChat(getChatId(update));

        return StringUtils.equalsIgnoreCase(
                botChat.chatStatus(),
                "CREATED"
        ) && StringUtils.equalsIgnoreCase(
                getMessage(update),
                "/digest"
        );
    }
}
