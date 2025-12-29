package com.example.minix.application.handler;

import com.example.minix.application.port.out.FollowRepository;
import com.example.minix.application.port.out.TimelineRepository;
import com.example.minix.domain.event.TweetCreatedEvent;
import com.example.minix.domain.model.TimelineItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handles TweetCreatedEvent and raises the tweet created event.
 */
@Component
@Slf4j
public class TweetCreatedEventHandler implements EventHandler<TweetCreatedEvent> {

    private final FollowRepository followRepository;
    private final TimelineRepository timelineRepository;

    public TweetCreatedEventHandler(FollowRepository followRepository,
            TimelineRepository timelineRepository) {
        this.followRepository = followRepository;
        this.timelineRepository = timelineRepository;
    }

    /**
     * Handles the tweet created event.
     *
     * @param event the tweet created event
     */
    public void handle(TweetCreatedEvent event) {

        log.debug("Tweet created for user {}", event.authorId());
        List<String> followers = followRepository.getFollowers(event.authorId());

        followers.parallelStream().forEach(followerId -> {
            // each timeline item needs to be a new timelime item for each user
            TimelineItem item = new TimelineItem(
                    event.tweetId(),
                    event.authorId(),
                    event.content(),
                    event.createdAt());
            timelineRepository.addToTimeline(followerId, item);
        });
    }
}
