package dev.abarmin.bots.service.support;

import dev.abarmin.bots.service.support.response.BotResponse;

public interface BotOperation {
    BotResponse process(BotRequest request);

    default boolean supports(BotRequest request) {
        return false;
    }
}
