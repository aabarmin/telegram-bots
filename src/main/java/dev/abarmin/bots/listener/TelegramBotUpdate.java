package dev.abarmin.bots.listener;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("TELEGRAM_UPDATES")
public record TelegramBotUpdate(
        @Column("ID") @Id Integer id,
        @Column("UPDATE_ID") Integer updateId,
        @Column("CHAT_ID") Long chatId,
        @Column("CREATED_AT") @CreatedDate LocalDateTime createdAt,
        @Column("UPDATED_AT") @LastModifiedDate LocalDateTime updatedAt
        ) {
}
