package dev.abarmin.bots.rss.digest.operation;

import com.pengrad.telegrambot.model.Update;
import dev.abarmin.bots.core.BotHelper;
import dev.abarmin.bots.core.BotOperation;
import dev.abarmin.bots.service.TelegramChatService;
import dev.abarmin.bots.rss.digest.DigestBotUpdate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResetHandler implements BotOperation {
    private final BotHelper helper;
    private final TelegramChatService chatService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void process(Update update) {
        if (isReset(update)) {
            chatService.updateStatus(helper.getChat(update), "CREATED");
            eventPublisher.publishEvent(new DigestBotUpdate(update));
        }
    }

    private boolean isReset(Update update) {
        var message = helper.getMessage(update);
        var chat = helper.getChat(update);

        return !StringUtils.equalsIgnoreCase(chat.chatStatus(), "CREATED") &&
                StringUtils.equalsIgnoreCase(message, "/start");
    }
}
