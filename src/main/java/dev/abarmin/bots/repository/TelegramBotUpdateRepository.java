package dev.abarmin.bots.repository;

import dev.abarmin.bots.rss.persistence.TelegramBotUpdate;
import org.springframework.data.repository.ListCrudRepository;

public interface TelegramBotUpdateRepository extends ListCrudRepository<TelegramBotUpdate, Integer> {
    boolean existsByUpdateId(int updateId);
}
