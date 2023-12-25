package dev.abarmin.bots.listener.service;

import com.pengrad.telegrambot.model.Update;
import dev.abarmin.bots.listener.TelegramBotUpdate;
import dev.abarmin.bots.listener.persistence.TelegramBotUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TelegramUpdateServiceImpl implements TelegramUpdateService {
    private final TelegramBotUpdateRepository repository;

    @Override
    public boolean isProcessed(Update update) {
        return repository.existsByUpdateId(update.updateId());
    }

    @Override
    public Update save(Update update) {
        final TelegramBotUpdate botUpdate = new TelegramBotUpdate(
                null,
                update.updateId(),
                update.message().chat().id(),
                LocalDateTime.now(),
                null
        );
        repository.save(botUpdate);
        return update;
    }
}
