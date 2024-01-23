package dev.abarmin.bots.service.digest;

import dev.abarmin.bots.entity.telegram.TelegramBotChat;
import dev.abarmin.bots.model.response.SendMessageResponse;
import dev.abarmin.bots.service.support.MessageSourceHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CreatedStateHandlerTest {
    @Mock
    MessageSourceHelper messageSource;

    @InjectMocks
    CreatedStateHandler uut;

    @InjectMocks
    BotRequestFactory factory;

    @Test
    void check_contextStarts() {
        assertThat(uut).isNotNull();
    }

    @Test
    void noState_processUnknown() {
        var response = uut.process(factory.newRequest(
                "Random message",
                new TelegramBotChat(10, "CREATED")
        ));

        assertThat(response)
                .isNotNull()
                .isInstanceOf(SendMessageResponse.class);
    }
}