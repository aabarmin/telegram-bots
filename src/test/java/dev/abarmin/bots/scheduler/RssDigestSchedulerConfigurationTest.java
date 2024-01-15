package dev.abarmin.bots.scheduler;

import dev.abarmin.bots.service.updates.UpdatesSender;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RssDigestSchedulerConfigurationTest {
    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withBean(RssDigestScheduler.class)
            .withBean(UpdatesSender.class, () -> mock(UpdatesSender.class));

    @Test
    void check_enabledByDefault() {
        runner.run(context -> {
            assertThat(context).hasSingleBean(RssDigestScheduler.class);
        });
    }

    @Test
    void check_canDisable() {
        runner.withPropertyValues("telegram.bot.rss.digest.scheduler.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(RssDigestScheduler.class);
                });
    }
}