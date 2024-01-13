package dev.abarmin.bots.repository;

import dev.abarmin.bots.rss.persistence.TelegramBotChat;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Optional;

public interface TelegramBotChatRepository extends
        ListCrudRepository<TelegramBotChat, Integer>,
        ListPagingAndSortingRepository<TelegramBotChat, Integer> {

    /**
     * Find chat by chat id.
     * @param chatId
     * @return
     */
    Optional<TelegramBotChat> findByChatId(long chatId);
}
