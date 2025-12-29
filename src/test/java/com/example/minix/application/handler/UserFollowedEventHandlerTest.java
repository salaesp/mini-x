package com.example.minix.application.handler;

import com.example.minix.application.port.out.TimelineRepository;
import com.example.minix.application.port.out.TweetRepository;
import com.example.minix.domain.event.UserFollowedEvent;
import com.example.minix.domain.model.TimelineItem;
import com.example.minix.domain.model.Tweet;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserFollowedEventHandlerTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private TimelineRepository timelineRepository;

    @InjectMocks
    private UserFollowedEventHandler eventHandler;

    @Test
    public void testHandle_ShouldBackfillTimelineWithRecentTweets() {
        String followerId = "follower1";
        String followedId = "followed1";
        UserFollowedEvent event = new UserFollowedEvent(followerId, followedId);

        Instant oldestTweetTimestamp = Instant.parse("2023-01-01T00:00:00Z");

        Tweet tweet1 = new Tweet("id1", followedId, "content1", Instant.now());
        Tweet tweet2 = new Tweet("id2", followedId, "content2", Instant.now());
        List<Tweet> recentTweets = Arrays.asList(tweet1, tweet2);

        when(tweetRepository.findByAuthorIdAfter(eq(followedId), anyInt()))
                .thenReturn(recentTweets);

        // Execute
        eventHandler.handle(event);

        // Validations
        verify(tweetRepository).findByAuthorIdAfter(eq(followedId), eq(50));

        ArgumentCaptor<TimelineItem> timelineItemCaptor = ArgumentCaptor.forClass(TimelineItem.class);
        verify(timelineRepository, times(2)).addToTimeline(eq(followerId), timelineItemCaptor.capture());

        List<TimelineItem> capturedItems = timelineItemCaptor.getAllValues();
        assertEquals(2, capturedItems.size());
        assertEquals("id1", capturedItems.get(0).tweetId());
        assertEquals("id2", capturedItems.get(1).tweetId());
    }

    @Test
    public void testHandle_ShouldUseEpochIfNoOldestTweetTimestamp() {
        String followerId = "followerNew";
        String followedId = "followed1";
        UserFollowedEvent event = new UserFollowedEvent(followerId, followedId);

        when(tweetRepository.findByAuthorIdAfter(eq(followedId), anyInt()))
                .thenReturn(List.of());

        // actual call
        eventHandler.handle(event);

        // Validations
        verify(tweetRepository).findByAuthorIdAfter(eq(followedId), eq(50));
    }
}
