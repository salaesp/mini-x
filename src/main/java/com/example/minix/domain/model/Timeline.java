package com.example.minix.domain.model;

import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;

public class Timeline {

    private final NavigableSet<TimelineItem> items;

    public Timeline() {
        this.items = new TreeSet<>(
                Comparator.comparing(TimelineItem::createdAt)
                        .thenComparing(TimelineItem::tweetId)
        );
    }

    public synchronized void add(TimelineItem item) {
        Objects.requireNonNull(item, "Timeline item cannot be null");
        items.add(item);
    }

    public Optional<TimelineItem> oldest() {
        return this.isEmpty()
                ? Optional.empty()
                : Optional.of(items.first());
    }

    public synchronized List<TimelineItem> items(int max) {
        return items.descendingSet()
                .stream()
                .limit(max)
                .toList();
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}
