package com.example.minix.domain.model;

import org.springframework.util.Assert;

import java.time.Instant;

/**
 * Domain model representing a tweet in a user's timeline.
 */
public record TimelineItem(String tweetId, String authorId, String content,
                           Instant createdAt) implements Comparable<TimelineItem> {

    public TimelineItem {

        Assert.hasText(tweetId, "Tweet ID cannot be empty");
        Assert.hasText(authorId, "Author ID cannot be empty");
        Assert.hasText(content, "Content cannot be empty");
        Assert.notNull(createdAt, "Created at cannot be null");
    }

    /**
     * Sort by creation time, newest first (reverse chronological).
     */
    @Override
    public int compareTo(TimelineItem other) {
        return other.createdAt.compareTo(this.createdAt);
    }
}
