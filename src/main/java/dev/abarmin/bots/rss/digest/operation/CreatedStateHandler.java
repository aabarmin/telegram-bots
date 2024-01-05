package dev.abarmin.bots.rss.digest.operation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.core.BotHelper;
import dev.abarmin.bots.core.BotOperation;
import dev.abarmin.bots.core.MessageSourceHelper;
import dev.abarmin.bots.listener.service.TelegramChatService;
import dev.abarmin.bots.rss.digest.DigestBotUpdate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatedStateHandler implements BotOperation {
    private final DigestOperationProcessor buildDigestOperation;
    private final ApplicationEventPublisher eventPublisher;
    private final TelegramChatService chatService;
    private final MessageSourceHelper messageSource;
    private final TelegramBot telegramBot;
    private final BotHelper helper;

    @Override
    public void process(Update update) {
        if (isStart(update) || isBack(update)) {
            processStart(update);
        } else if (isDigest(update)) {
            buildDigestOperation.process(update);
        } else if (isSubscriptions(update)) {
            processSubscriptions(update);
        } else {
            processUnknown(update);
        }
    }

    private void processSubscriptions(Update update) {
        chatService.updateStatus(helper.getChat(update), "SUBSCRIPTIONS");
        eventPublisher.publishEvent(new DigestBotUpdate(update));
    }

    private boolean isStart(Update update) {
        return StringUtils.equalsIgnoreCase(helper.getMessage(update), "/start");
    }

    private boolean isDigest(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.digest", update)
        );
    }

    private boolean isBack(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.back", update)
        );
    }

    private boolean isSubscriptions(Update update) {
        return StringUtils.equalsIgnoreCase(
                helper.getMessage(update),
                messageSource.getMessage("bot.digest.button.subscriptions", update)
        );
    }

    private void processUnknown(Update update) {
        var message = new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.not-supported", update)
        );
        telegramBot.execute(message);
    }

    private void processStart(Update update) {
        var message = new SendMessage(
                helper.getChatId(update),
                messageSource.getMessage("bot.digest.start", update)
        )
                .replyMarkup(new ReplyKeyboardMarkup(
                        new KeyboardButton(
                                messageSource.getMessage("bot.digest.button.digest", update)
                        ),
                        new KeyboardButton(
                                messageSource.getMessage("bot.digest.button.subscriptions", update)
                        )
                ));
        telegramBot.execute(message);
    }

    @Override
    public boolean supports(Update update) {
        var chat = chatService.findChat(helper.getChatId(update));
        return StringUtils.equalsIgnoreCase(chat.chatStatus(), "CREATED");
    }
}
