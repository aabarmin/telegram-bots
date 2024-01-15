package dev.abarmin.bots.scheduler;

import dev.abarmin.bots.service.updates.UpdatesSender;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        prefix = "telegram.bot.rss.digest.scheduler",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@RequiredArgsConstructor
public class RssDigestScheduler {
    private final UpdatesSender updatesSender;

    @Scheduled(cron = "${telegram.bot.rss.digest.scheduler.cronExpression}")
    public void sendDigest() {
        updatesSender.sendUpdates();
    }
}
