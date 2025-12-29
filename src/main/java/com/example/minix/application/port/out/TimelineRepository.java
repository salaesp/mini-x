package com.example.minix.application.port.out;

import com.example.minix.domain.model.TimelineItem;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Port interface for timeline persistence operations.
 * Defines the contract for storing and retrieving user timelines.
 */
public interface TimelineRepository {
    
    /**
     * Adds a timeline item to a user's timeline.
     *
     * @param userId the user ID
     * @param item the timeline item to add
     */
    void addToTimeline(String userId, TimelineItem item);
    
    /**
     * Gets a user's timeline with a limit on the number of items.
     *
     * @param userId the user ID
     * @param limit maximum number of items to return
     * @return list of timeline items (newest first)
     */
    List<TimelineItem> getTimeline(String userId, int limit);
}
