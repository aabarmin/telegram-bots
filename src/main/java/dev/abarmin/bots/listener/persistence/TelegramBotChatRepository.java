package dev.abarmin.bots.listener.persistence;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface TelegramBotChatRepository extends ListCrudRepository<TelegramBotChat, Integer> {
    Optional<TelegramBotChat> findByChatId(long chatId);
}
