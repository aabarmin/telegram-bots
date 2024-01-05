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

    @Test
    void rssListener_canConsumeMessage() {
		long chatId = new Random().nextLong();
		publisher.publish("/start", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Hello, I am an RSS Reader!");
		});
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
			assertThat(responseText).contains("Hello, I am an RSS Reader!");
		});
	}

	@Test
	void rssListener_deleteSubscription() {
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

		// add subscription
		publisher.publish("Add subscription", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Enter URL of a subscription or Back to return");
		});

		// add valid URL
		publisher.publish("https://www.reddit.com/r/programming/.rss", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Added to your subscriptions");
		});

		// to delete subscriptions
		publisher.publish("Delete subscription", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Reply with a number of a subscription to cancel (or Back to return):");
		});

		// select the first
		publisher.publish("1", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Managing subscriptions");
		});
	}

	@Test
	void rssListener_addSubscription() {
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

		// add subscription
		publisher.publish("Add subscription", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Enter URL of a subscription or Back to return");
		});

		// back to the main menu
		publisher.publish("Back", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Hello, I am an RSS Reader!");
		});

		// move to subscriptions
		publisher.publish("My subscriptions", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Managing subscriptions");
		});

		// back to adding
		publisher.publish("Add subscription", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Enter URL of a subscription or Back to return");
		});

		// add invalid URL
		publisher.publish("https://google.com", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Provided URL is not a valid RSS");
		});

		// add valid URL
		publisher.publish("https://www.reddit.com/r/programming/.rss", chatId, message -> {
			var responseText = (String) message.getParameters().get("text");
			assertThat(responseText).contains("Added to your subscriptions");
		});
	}
}
