package com.example.minix.infrastructure.in.web.dto;

import com.example.minix.domain.model.TimelineItem;

import java.time.Instant;

/**
 * DTO for timeline item response.
 */
public record TimelineItemResponse(
        String tweetId,
        String authorId,
        String content,
        Instant createdAt
) {
    public static TimelineItemResponse fromDomain(TimelineItem item) {
        return new TimelineItemResponse(
            item.tweetId(),
            item.authorId(),
            item.content(),
            item.createdAt()
        );
    }
}
