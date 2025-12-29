package com.example.minix.application.usecase;

import com.example.minix.application.port.out.TimelineRepository;
import com.example.minix.domain.model.TimelineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GetTimelineUseCase.
 */
@ExtendWith(MockitoExtension.class)
public class GetTimelineUseCaseTest {

    @Mock
    private TimelineRepository timelineRepository;

    private GetTimelineUseCase useCase;

    private static final int MAX_TIMELINE_LENGTH = 3;

    @BeforeEach
    void setUp() {
        useCase = new GetTimelineUseCase(timelineRepository, MAX_TIMELINE_LENGTH);
    }

    @Test
    public void testExecute_shouldReturnTimelineForUser() {
        // Configuring test
        String userId = "user123";
        Instant now = Instant.now();
        List<TimelineItem> expectedTimeline = Arrays.asList(
                new TimelineItem("tweet1", "author1", "content1", now),
                new TimelineItem("tweet2", "author2", "content2", now.minusSeconds(60)),
                new TimelineItem("tweet3", "author3", "content3", now.minusSeconds(120))
        );

        when(timelineRepository.getTimeline(userId, MAX_TIMELINE_LENGTH)).thenReturn(expectedTimeline);

        // Execution
        List<TimelineItem> result = useCase.execute(userId);

        // Verifications
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(expectedTimeline, result);

        verify(timelineRepository, times(1)).getTimeline(userId, MAX_TIMELINE_LENGTH);
    }

    @Test
    public void testExecute_shouldReturnEmptyListWhenTimelineIsEmpty() {
        // Configuring test
        String userId = "user123";
        when(timelineRepository.getTimeline(userId, MAX_TIMELINE_LENGTH)).thenReturn(Collections.emptyList());

        // Execution
        List<TimelineItem> result = useCase.execute(userId);

        // Verifications
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(timelineRepository, times(1)).getTimeline(userId, MAX_TIMELINE_LENGTH);
    }

    @Test
    public void testExecute_shouldHandleDifferentUserIds() {
        // Configuring test
        String userId1 = "user1";
        String userId2 = "user2";
        Instant now = Instant.now();

        List<TimelineItem> timeline1 = List.of(
                new TimelineItem("tweet1", "author1", "content1", now)
        );
        List<TimelineItem> timeline2 = List.of(
                new TimelineItem("tweet2", "author2", "content2", now)
        );

        when(timelineRepository.getTimeline(userId1, MAX_TIMELINE_LENGTH)).thenReturn(timeline1);
        when(timelineRepository.getTimeline(userId2, MAX_TIMELINE_LENGTH)).thenReturn(timeline2);

        // Execution
        List<TimelineItem> result1 = useCase.execute(userId1);
        List<TimelineItem> result2 = useCase.execute(userId2);

        // Verifications
        assertEquals(timeline1, result1);
        assertEquals(timeline2, result2);
        assertNotEquals(result1, result2);

        verify(timelineRepository).getTimeline(userId1, MAX_TIMELINE_LENGTH);
        verify(timelineRepository).getTimeline(userId2, MAX_TIMELINE_LENGTH);
    }
}
