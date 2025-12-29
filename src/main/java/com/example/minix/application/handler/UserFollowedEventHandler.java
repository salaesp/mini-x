package com.example.minix.application.handler;

import com.example.minix.application.port.out.TimelineRepository;
import com.example.minix.application.port.out.TweetRepository;
import com.example.minix.domain.event.UserFollowedEvent;
import com.example.minix.domain.model.TimelineItem;
import com.example.minix.domain.model.Tweet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handles UserFollowedEvent by backfilling the follower's timeline.
 * Adds recent tweets from the followed user to the follower's timeline.
 */
@Component
@Slf4j
public class UserFollowedEventHandler implements EventHandler<UserFollowedEvent> {

    private static final int MAX_BACKFILL_TWEETS = 50;

    private final TweetRepository tweetRepository;
    private final TimelineRepository timelineRepository;

    public UserFollowedEventHandler(TweetRepository tweetRepository,
            TimelineRepository timelineRepository) {
        this.tweetRepository = tweetRepository;
        this.timelineRepository = timelineRepository;
    }

    /**
     * Handles the user followed event.
     * Backfills the follower's timeline with recent tweets from the followed user.
     * <p>
     * Strategy:
     * 1. Get the oldest tweet timestamp in the follower's timeline
     * 2. Fetch tweets from the followed user created after that timestamp
     * 3. Add those tweets to the follower's timeline
     * <p>
     * This avoids fetching all tweets and only gets relevant ones.
     *
     * @param event the user followed event
     */
    public void handle(UserFollowedEvent event) {
        log.debug("User {} followed {}", event.followerId(), event.followedId());
        String followerId = event.followerId();
        String followedId = event.followedId();

        log.debug("Backfilling tweets for user timeline");
        // Get recent tweets from the followed user created after the oldest timeline
        // tweet
        List<Tweet> tweetsToBackfill = tweetRepository.findByAuthorIdAfter(
                followedId,
                MAX_BACKFILL_TWEETS);

        // Add each tweet to the follower's timeline
        for (Tweet tweet : tweetsToBackfill) {
            TimelineItem item = new TimelineItem(
                    tweet.id(),
                    tweet.authorId(),
                    tweet.content(),
                    tweet.createdAt());
            timelineRepository.addToTimeline(followerId, item);
        }
    }
}
