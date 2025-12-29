package com.example.minix.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TimelineItem domain model.
 */
class TimelineItemTest {

    @Test
    public void testConstructor_shouldCreateTimelineItemWithValidData() {
        // Configuring test
        String tweetId = "tweet123";
        String authorId = "user123";
        String content = "Test content";
        Instant createdAt = Instant.now();

        // Execution
        TimelineItem item = new TimelineItem(tweetId, authorId, content, createdAt);

        // Verifications
        assertEquals(tweetId, item.tweetId());
        assertEquals(authorId, item.authorId());
        assertEquals(content, item.content());
        assertEquals(createdAt, item.createdAt());
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenTweetIdIsNull() {
        // Configuring test
        String tweetId = null;
        String authorId = "user123";
        String content = "Test content";
        Instant createdAt = Instant.now();

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimelineItem(tweetId, authorId, content, createdAt)
        );
        assertTrue(exception.getMessage().contains("Tweet ID cannot be empty"));
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenTweetIdIsEmpty() {
        // Configuring test
        String tweetId = "";
        String authorId = "user123";
        String content = "Test content";
        Instant createdAt = Instant.now();

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimelineItem(tweetId, authorId, content, createdAt)
        );
        assertTrue(exception.getMessage().contains("Tweet ID cannot be empty"));
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenAuthorIdIsNull() {
        // Configuring test
        String tweetId = "tweet123";
        String authorId = null;
        String content = "Test content";
        Instant createdAt = Instant.now();

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimelineItem(tweetId, authorId, content, createdAt)
        );
        assertTrue(exception.getMessage().contains("Author ID cannot be empty"));
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenAuthorIdIsEmpty() {
        // Configuring test
        String tweetId = "tweet123";
        String authorId = "";
        String content = "Test content";
        Instant createdAt = Instant.now();

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimelineItem(tweetId, authorId, content, createdAt)
        );
        assertTrue(exception.getMessage().contains("Author ID cannot be empty"));
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenContentIsNull() {
        // Configuring test
        String tweetId = "tweet123";
        String authorId = "user123";
        String content = null;
        Instant createdAt = Instant.now();

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimelineItem(tweetId, authorId, content, createdAt)
        );
        assertTrue(exception.getMessage().contains("Content cannot be empty"));
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenContentIsEmpty() {
        // Configuring test
        String tweetId = "tweet123";
        String authorId = "user123";
        String content = "";
        Instant createdAt = Instant.now();

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimelineItem(tweetId, authorId, content, createdAt)
        );
        assertTrue(exception.getMessage().contains("Content cannot be empty"));
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenCreatedAtIsNull() {
        // Configuring test
        String tweetId = "tweet123";
        String authorId = "user123";
        String content = "Test content";
        Instant createdAt = null;

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimelineItem(tweetId, authorId, content, createdAt)
        );
        assertTrue(exception.getMessage().contains("Created at cannot be null"));
    }

    @Test
    public void testConstructor_shouldSortByCreatedAtNewestFirst() {
        // Configuring test
        Instant now = Instant.now();
        Instant earlier = now.minusSeconds(3600);
        Instant latest = now.plusSeconds(3600);

        TimelineItem item1 = new TimelineItem("tweet1", "user1", "content1", earlier);
        TimelineItem item2 = new TimelineItem("tweet2", "user2", "content2", now);
        TimelineItem item3 = new TimelineItem("tweet3", "user3", "content3", latest);

        List<TimelineItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        // Execution
        Collections.sort(items);

        // Verifications
        assertEquals("tweet3", items.get(0).tweetId()); // Latest first
        assertEquals("tweet2", items.get(1).tweetId());
        assertEquals("tweet1", items.get(2).tweetId()); // Earliest last
    }

    @Test
    public void testConstructor_shouldCompareCorrectlyWhenCreatedAtIsSame() {
        // Configuring test
        Instant now = Instant.now();
        TimelineItem item1 = new TimelineItem("tweet1", "user1", "content1", now);
        TimelineItem item2 = new TimelineItem("tweet2", "user2", "content2", now);

        // Execution
        int comparison = item1.compareTo(item2);

        // Verifications
        assertEquals(0, comparison);
    }

    @Test
    public void testConstructor_shouldReturnPositiveWhenThisIsOlder() {
        // Configuring test
        Instant now = Instant.now();
        Instant earlier = now.minusSeconds(3600);

        TimelineItem olderItem = new TimelineItem("tweet1", "user1", "content1", earlier);
        TimelineItem newerItem = new TimelineItem("tweet2", "user2", "content2", now);

        // Execution
        int comparison = olderItem.compareTo(newerItem);

        // Verifications
        assertTrue(comparison > 0); // Older items should come after newer ones
    }

    @Test
    public void testConstructor_shouldReturnNegativeWhenThisIsNewer() {
        // Configuring test
        Instant now = Instant.now();
        Instant earlier = now.minusSeconds(3600);

        TimelineItem newerItem = new TimelineItem("tweet1", "user1", "content1", now);
        TimelineItem olderItem = new TimelineItem("tweet2", "user2", "content2", earlier);

        // Execution
        int comparison = newerItem.compareTo(olderItem);

        // Verifications
        assertTrue(comparison < 0); // Newer items should come before older ones
    }
}
