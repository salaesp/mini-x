package com.example.minix.application.handler;

import com.example.minix.application.port.out.FollowRepository;
import com.example.minix.application.port.out.TimelineRepository;
import com.example.minix.domain.event.TweetCreatedEvent;
import com.example.minix.domain.model.TimelineItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TweetCreatedEventHandlerTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private TimelineRepository timelineRepository;

    @InjectMocks
    private TweetCreatedEventHandler eventHandler;

    @Test
    public void testHandle_shouldAddTweetToFollowersTimeline() {
        String authorId = "author1";
        String tweetId = "tweet1";
        String content = "Hello World";
        Instant createdAt = Instant.now();
        TweetCreatedEvent event = new TweetCreatedEvent(tweetId, authorId, content, createdAt);

        List<String> followers = Arrays.asList("followerA", "followerB");
        when(followRepository.getFollowers(authorId)).thenReturn(followers);

        // Execute
        eventHandler.handle(event);

        // Validations
        verify(followRepository).getFollowers(authorId);

        ArgumentCaptor<TimelineItem> timelineItemCaptor = ArgumentCaptor.forClass(TimelineItem.class);

        // Verify addToTimeline is called for each follower
        verify(timelineRepository).addToTimeline(eq("followerA"), timelineItemCaptor.capture());
        verify(timelineRepository).addToTimeline(eq("followerB"), timelineItemCaptor.capture());

        // Check the captured item (should be the same for all calls in this context
        // logic)
        TimelineItem capturedItem = timelineItemCaptor.getValue();
        assertEquals(tweetId, capturedItem.tweetId());
        assertEquals(authorId, capturedItem.authorId());
        assertEquals(content, capturedItem.content());
        assertEquals(createdAt, capturedItem.createdAt());
    }

    @Test
    public void testHandle_shouldDoNothingIfNoFollowers() {
        // Arrange
        String authorId = "author2";
        TweetCreatedEvent event = new TweetCreatedEvent("tweet2", authorId, "Content", Instant.now());

        when(followRepository.getFollowers(authorId)).thenReturn(List.of());

        // Act
        eventHandler.handle(event);

        // Assert
        verify(followRepository).getFollowers(authorId);
        verify(timelineRepository, times(0)).addToTimeline(any(), any());
    }
}
