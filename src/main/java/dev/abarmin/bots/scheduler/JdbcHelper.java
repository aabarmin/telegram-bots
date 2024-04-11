package dev.abarmin.bots.scheduler;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@UtilityClass
public class JdbcHelper {
    public static LocalDateTime readLocalDateTime(Timestamp timestamp) {
        return LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
    }
}
