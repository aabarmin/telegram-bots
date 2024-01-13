package dev.abarmin.bots.repository;

import dev.abarmin.bots.entity.telegram.TelegramBotUpdate;
import org.springframework.data.repository.ListCrudRepository;

public interface TelegramBotUpdateRepository extends ListCrudRepository<TelegramBotUpdate, Integer> {
    boolean existsByUpdateId(int updateId);
}
