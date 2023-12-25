package dev.abarmin.bots.listener.persistence;

import dev.abarmin.bots.listener.TelegramBotUpdate;
import org.springframework.data.repository.CrudRepository;

public interface TelegramBotUpdateRepository extends CrudRepository<TelegramBotUpdate, Integer> {
    boolean existsByUpdateId(int updateId);
}
