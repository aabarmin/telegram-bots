package dev.abarmin.bots.publisher;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestMessage {
    String text;
    long chatId;
}
