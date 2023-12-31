package dev.abarmin.bots.rss.digest.operation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.core.BotHelper;
import dev.abarmin.bots.core.BotOperation;
import dev.abarmin.bots.core.MessageSourceHelper;
import dev.abarmin.bots.listener.persistence.TelegramBotChat;
import dev.abarmin.bots.listener.service.TelegramChatService;
import dev.abarmin.bots.rss.digest.service.SubscriptionService;
import dev.abarmin.bots.rss.reader.persistence.ArticleSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeleteSubscriptionOperation implements BotOperation {
    private final BotHelper helper;
    private final TelegramBot telegramBot;
    private final TelegramChatService chatService;
    private final MessageSourceHelper messageSource;
    private final SubscriptionService subscriptionService;

    @Override
    public void process(Update update) {
        var subscriptions = subscriptions(helper.getChat(update))
                .entrySet()
                .stream()
                .map(entry -> String.format("%s. %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));

        var message = new SendMessage(
                helper.getChatId(update),
                "Reply with a number of a subscription to cancel (or Back to return): \n\n" + subscriptions
        ).replyMarkup(new ReplyKeyboardRemove());

        telegramBot.execute(message);
        chatService.updateStatus(helper.getChat(update), "SUBSCRIPTIONS_DELETE");
    }

    private Map<Integer, String> subscriptions(TelegramBotChat chat) {
        var subscriptions = new HashMap<Integer, String>();
        int index = 1;
        for (ArticleSource subscription : subscriptionService.findSubscriptions(chat)) {
            subscriptions.put(index++, subscription.sourceName());
        }
        return subscriptions;
    }

    @Override
    public boolean supports(Update update) {
        var chat = helper.getChat(update);
        var message = helper.getMessage(update);

        return StringUtils.equalsIgnoreCase(chat.chatStatus(), "SUBSCRIPTIONS") &&
                StringUtils.equalsIgnoreCase(message, messageSource.getMessage("bot.digest.button.subscriptions-delete", update));
    }
}
