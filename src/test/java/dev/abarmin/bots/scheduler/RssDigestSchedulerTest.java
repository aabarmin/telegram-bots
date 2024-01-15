package dev.abarmin.bots.scheduler;

import dev.abarmin.bots.service.updates.UpdatesSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RssDigestSchedulerTest {
    @InjectMocks
    private RssDigestScheduler scheduler;

    @Mock
    private UpdatesSender updatesSender;

    @Test
    void scheduler_callsService() {
        doNothing().when(updatesSender).sendUpdates();

        scheduler.sendDigest();

        verify(updatesSender, times(1)).sendUpdates();
    }
}