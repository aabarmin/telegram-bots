package dev.abarmin.bots.publisher;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.abarmin.bots.model.DigestBotUpdate;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Random;
import java.util.function.Consumer;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@Component
public class MessagePublisher {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private TelegramBot telegramBot;

    private int updateId = new Random().nextInt();

    public void publish(String message, long chatId, Consumer<SendMessage> validator) {
        var testMessage = TestMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();

        publish(testMessage, validator);
    }

    public void publish(TestMessage message, Consumer<SendMessage> validator) {
        final var update = new Update();
        final var telegramMessage = new Message();
        final var chat = new Chat();

        ReflectionTestUtils.setField(update, "update_id", updateId++); // need to have always increasing numbers
        ReflectionTestUtils.setField(chat, "id", message.getChatId());
        ReflectionTestUtils.setField(telegramMessage, "chat", chat);
        ReflectionTestUtils.setField(telegramMessage, "text", message.getText());
        ReflectionTestUtils.setField(update, "message", telegramMessage);

        eventPublisher.publishEvent(new DigestBotUpdate(update));

        var captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, atLeastOnce()).execute(captor.capture());

        validator.accept(captor.getValue());
    }
}
