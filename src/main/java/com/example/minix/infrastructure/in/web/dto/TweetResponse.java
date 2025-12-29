package com.example.minix.infrastructure.in.web.dto;

import com.example.minix.domain.model.Tweet;

import java.time.Instant;

/**
 * DTO for tweet response.
 */
public record TweetResponse(
        String id,
        String authorId,
        String content,
        Instant createdAt
) {
    public static TweetResponse fromDomain(Tweet tweet) {
        return new TweetResponse(
            tweet.id(),
            tweet.authorId(),
            tweet.content(),
            tweet.createdAt()
        );
    }
}
