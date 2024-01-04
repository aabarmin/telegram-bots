package dev.abarmin.bots;

import com.pengrad.telegrambot.TelegramBot;
import dev.abarmin.bots.listener.persistence.TelegramBotChatRepository;
import dev.abarmin.bots.publisher.MessagePublisher;
import dev.abarmin.bots.publisher.TestMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "telegram.bot.listener.webhook.enabled=false",
        "telegram.bot.listener.polling.enabled=false",
		"telegram.bot.rss.reader.enabled=false"
})
@MockBeans({
		@MockBean(TelegramBot.class)
})
class BotsApplicationRssDigestTest {
    @Autowired
    private MessagePublisher publisher;

	@Autowired
	private TelegramBotChatRepository chatRepository;

    @Test
    void rssListener_canConsumeMessage() {
		long chatId = new Random().nextLong();
		publisher.publish("/start", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Hello, I am an RSS Reader!");
		});

		var chat = chatRepository.findByChatId(chatId).orElseThrow();
		assertThat(chat.chatStatus()).isEqualTo("CREATED");
	}

	@Test
	void rssListener_getDigest() {
		var chatId = new Random().nextInt();

		// start the chat
		publisher.publish("/start", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Hello, I am an RSS Reader!");
		});

		// request a digest
		publisher.publish("Get a digest", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Digest");
		});

		// move to subscriptions
		publisher.publish("My subscriptions", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Managing subscriptions");
		});

		// move back
		publisher.publish("Back", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Getting back to the main menu");
		});
	}

	@Test
	void rssListener_manageSubscriptions() {
		var chatId = new Random().nextInt();

		// start the chat
		publisher.publish("/start", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Hello, I am an RSS Reader!");
		});

		// move to subscriptions
		publisher.publish("My subscriptions", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Managing subscriptions");
		});

		// move back
		publisher.publish("Back", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Getting back to the main menu");
		});
	}
}
