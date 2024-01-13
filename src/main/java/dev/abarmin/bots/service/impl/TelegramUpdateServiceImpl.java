package dev.abarmin.bots.service.impl;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import dev.abarmin.bots.rss.persistence.TelegramBotUpdate;
import dev.abarmin.bots.rss.persistence.TelegramBotUpdateRepository;
import dev.abarmin.bots.service.TelegramChatService;
import dev.abarmin.bots.service.TelegramUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TelegramUpdateServiceImpl implements TelegramUpdateService {
    private final TelegramBotUpdateRepository updateRepository;
    private final TelegramChatService chatService;

    @Override
    public boolean isProcessed(Update update) {
        return updateRepository.existsByUpdateId(update.updateId());
    }

    @Override
    public Update save(Update update) {
        long chatId = Optional.of(update)
                .map(Update::message)
                .map(Message::chat)
                .map(Chat::id)
                .orElse(0L);

        var chat = chatService.findChat(chatId);

        final TelegramBotUpdate botUpdate = new TelegramBotUpdate(
                null,
                update.updateId(),
                AggregateReference.to(chat.chatId()),
                LocalDateTime.now(),
                null
        );

        updateRepository.save(botUpdate);
        return update;
    }
}
