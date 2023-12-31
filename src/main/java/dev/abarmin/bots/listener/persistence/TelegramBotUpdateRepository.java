package dev.abarmin.bots.listener.persistence;

import org.springframework.data.repository.ListCrudRepository;

public interface TelegramBotUpdateRepository extends ListCrudRepository<TelegramBotUpdate, Integer> {
    boolean existsByUpdateId(int updateId);
}
