package dev.abarmin.bots.service.impl;

import dev.abarmin.bots.entity.telegram.TelegramBotChat;
import dev.abarmin.bots.repository.TelegramBotChatRepository;
import dev.abarmin.bots.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramChatServiceImpl implements TelegramChatService {
    private final TelegramBotChatRepository repository;

    @Override
    public TelegramBotChat findChat(long chatId) {
        return repository.findByChatId(chatId)
                .orElseGet(() -> repository.save(new TelegramBotChat(
                        chatId,
                        "CREATED"
                )));
    }

    @Override
    public TelegramBotChat updateStatus(TelegramBotChat chat, String status) {
        var updatedChat = chat.withStatus(status);
        return repository.save(updatedChat);
    }
}
