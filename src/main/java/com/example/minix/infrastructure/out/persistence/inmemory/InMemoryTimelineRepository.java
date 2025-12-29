package com.example.minix.infrastructure.out.persistence.inmemory;

import com.example.minix.application.port.out.TimelineRepository;
import com.example.minix.domain.model.Timeline;
import com.example.minix.domain.model.TimelineItem;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of TimelineRepository.
 * Stores materialized timelines for read optimization.
 * Thread-safe using ConcurrentHashMap and synchronized lists.
 */
@Repository
public class InMemoryTimelineRepository implements TimelineRepository {

    private final ConcurrentHashMap<String, Timeline> timelines = new ConcurrentHashMap<>();

    @Override
    public void addToTimeline(String userId, TimelineItem item) {
        timelines.computeIfAbsent(userId, k ->
                        new Timeline())
                .add(item);
    }

    @Override
    public List<TimelineItem> getTimeline(String userId, int limit) {
        Timeline timeline = timelines.get(userId);

        if (timeline == null) {
            return Collections.emptyList();
        }
        return timeline.items(limit);
    }
}
