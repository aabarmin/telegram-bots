package dev.abarmin.bots.listener.service;

import dev.abarmin.bots.listener.persistence.TelegramBotChat;
import dev.abarmin.bots.listener.persistence.TelegramBotChatRepository;
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
