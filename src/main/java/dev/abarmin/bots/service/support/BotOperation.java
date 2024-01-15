package dev.abarmin.bots.service.support;

import dev.abarmin.bots.model.request.BotRequest;
import dev.abarmin.bots.model.response.BotResponse;

public interface BotOperation {
    BotResponse process(BotRequest request);

    default boolean supports(BotRequest request) {
        return false;
    }
}
