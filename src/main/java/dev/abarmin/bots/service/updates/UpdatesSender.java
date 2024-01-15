package dev.abarmin.bots.service.updates;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.entity.telegram.TelegramBotChat;
import dev.abarmin.bots.service.DigestService;
import dev.abarmin.bots.service.SubscriptionService;
import dev.abarmin.bots.service.TelegramChatService;
import dev.abarmin.bots.service.digest.processor.DigestConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UpdatesSender {
    private final TelegramBot telegramBot;
    private final DigestService digestService;
    private final TelegramChatService chatService;
    private final SubscriptionService subscriptionService;
    private final DigestConverter digestConverter;

    public void sendUpdates() {
        var yesterdayDate = LocalDate.now().minusDays(1);
        for (TelegramBotChat chat : chatService.findAll()) {
            var subscriptions = subscriptionService.findSubscriptions(chat);
            if (subscriptions.isEmpty()) {
                continue;
            }
            var digest = digestService.create(chat, yesterdayDate);
            var content = digestConverter.toMarkdown(
                    digest,
                    "New articles since yesterday",
                    "Nothing to show"
            );

            telegramBot.execute(new SendMessage(
                    chat.chatId(),
                    content
            ).parseMode(ParseMode.Markdown));
        }
    }
}
