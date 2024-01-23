package dev.abarmin.bots.model.response;

import com.google.common.collect.Lists;

import java.util.Collection;

public record NoopResponse(
        Collection<BotResponse<?>> nextActions
) implements BotResponse<NoopResponse> {
    public NoopResponse() {
        this(Lists.newArrayList());
    }

    @Override
    public NoopResponse then(BotResponse<?> next) {
        this.nextActions.add(next);
        return this;
    }
}
