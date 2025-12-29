package com.example.minix.domain.model;

import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model representing a tweet.
 * Immutable and enforces business rules (280 character limit).
 */
public record Tweet(String id, String authorId, String content, Instant createdAt) {

    private static final int MAX_CONTENT_LENGTH = 280;

    /**
     * Creates a new tweet with validation.
     *
     * @param authorId the ID of the tweet author
     * @param content  the tweet content
     */
    public Tweet(String authorId, String content) {
        this(UUID.randomUUID().toString(), authorId, content, Instant.now());
    }

    /**
     * Constructor for reconstituting tweets (e.g., from storage).
     */
    public Tweet {
        validateContent(content);
        Assert.hasText(authorId, "Tweet ID cannot be empty");
    }

    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Tweet content cannot be empty");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Tweet content cannot exceed %d characters", MAX_CONTENT_LENGTH)
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return Objects.equals(id, tweet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
