package dev.abarmin.bots.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("TELEGRAM_CHATS")
public record TelegramBotChat(
        @Column("ID") @Id Integer id,
        @Column("CHAT_ID") Long chatId,
        @Column("CHAT_STATUS") String chatStatus,
        @Column("CREATED_AT") @CreatedDate LocalDateTime createdAt,
        @Column("UPDATED_AT") @LastModifiedDate LocalDateTime updatedAt
) {

    public TelegramBotChat(long chatId, String chatStatus) {
        this(
                null,
                chatId,
                chatStatus,
                LocalDateTime.now(),
                null
        );
    }

    public TelegramBotChat withStatus(String chatStatus) {
        return new TelegramBotChat(
                id(),
                chatId(),
                chatStatus,
                createdAt(),
                LocalDateTime.now()
        );
    }
}
