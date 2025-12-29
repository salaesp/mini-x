package com.example.minix.domain.event;

import java.time.Instant;

/**
 * Domain event representing a tweet creation.
 */
public record TweetCreatedEvent(String tweetId, String authorId,
                                String content, Instant createdAt) implements Event {
    @Override
    public EventType getType() {
        return EventType.TWEET_CREATED;
    }
}
