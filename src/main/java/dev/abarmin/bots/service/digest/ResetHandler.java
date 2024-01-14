package dev.abarmin.bots.service.digest;

import dev.abarmin.bots.model.DigestBotUpdate;
import dev.abarmin.bots.service.TelegramChatService;
import dev.abarmin.bots.service.support.BotOperation;
import dev.abarmin.bots.service.support.BotRequest;
import dev.abarmin.bots.service.support.response.BotResponse;
import dev.abarmin.bots.service.support.response.NoopResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResetHandler implements BotOperation {
    private final TelegramChatService chatService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public BotResponse process(BotRequest request) {
        if (isReset(request)) {
            chatService.updateStatus(request.chat(), "CREATED");
            eventPublisher.publishEvent(new DigestBotUpdate(request.update()));
        }

        return new NoopResponse();
    }

    private boolean isReset(BotRequest request) {
        return !StringUtils.equalsIgnoreCase(request.chat().chatStatus(), "CREATED") &&
                StringUtils.equalsIgnoreCase(request.message(), "/start");
    }
}
