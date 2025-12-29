package com.example.minix.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Tweet domain model.
 */
class TweetTest {

    @Test
    void testConstructor_shouldCreateTweetWithValidContent() {
        // Configuring test
        String authorId = "user123";
        String content = "This is a valid tweet";

        // Execution
        Tweet tweet = new Tweet(authorId, content);

        // Verifications
        assertNotNull(tweet.id());
        assertEquals(authorId, tweet.authorId());
        assertEquals(content, tweet.content());
        assertNotNull(tweet.createdAt());
    }

    @Test
    void testConstructor_shouldCreateTweetWithMaximumLength() {
        // Configuring test
        String authorId = "user123";
        String content = "a".repeat(280);

        // Execution
        Tweet tweet = new Tweet(authorId, content);

        // Verifications
        assertNotNull(tweet);
        assertEquals(280, tweet.content().length());
    }

    @Test
    void testConstructor_shouldThrowExceptionWhenContentExceedsMaxLength() {
        // Configuring test
        String authorId = "user123";
        String content = "a".repeat(281);

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Tweet(authorId, content)
        );
        assertTrue(exception.getMessage().contains("cannot exceed 280 characters"));
    }

    @Test
    void testConstructor_shouldThrowExceptionWhenContentIsNull() {
        // Configuring test
        String authorId = "user123";
        String content = null;

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Tweet(authorId, content)
        );
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    void testConstructor_shouldThrowExceptionWhenContentIsEmpty() {
        // Configuring test
        String authorId = "user123";
        String content = "";

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Tweet(authorId, content)
        );
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    void testConstructor_shouldThrowExceptionWhenContentIsBlank() {
        // Configuring test
        String authorId = "user123";
        String content = "   ";

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Tweet(authorId, content)
        );
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    void testConstructor_shouldThrowExceptionWhenAuthorIdIsNull() {
        // Configuring test
        String authorId = null;
        String content = "Valid content";

        // Execution & Then
        assertThrows(
            IllegalArgumentException.class,
            () -> new Tweet(authorId, content)
        );
    }

    @Test
    void testConstructor_shouldThrowExceptionWhenAuthorIdIsEmpty() {
        // Configuring test
        String authorId = "";
        String content = "Valid content";

        // Execution & Then
        assertThrows(
            IllegalArgumentException.class,
            () -> new Tweet(authorId, content)
        );
    }

    @Test
    void testConstructor_shouldReconstituteExistingTweet() {
        // Configuring test
        String id = "tweet123";
        String authorId = "user123";
        String content = "Existing tweet";
        Instant createdAt = Instant.now();

        // Execution
        Tweet tweet = new Tweet(id, authorId, content, createdAt);

        // Verifications
        assertEquals(id, tweet.id());
        assertEquals(authorId, tweet.authorId());
        assertEquals(content, tweet.content());
        assertEquals(createdAt, tweet.createdAt());
    }

    @Test
    void testConstructor_shouldBeEqualWhenIdsMatch() {
        // Configuring test
        String id = "tweet123";
        Tweet tweet1 = new Tweet(id, "user1", "content1", Instant.now());
        Tweet tweet2 = new Tweet(id, "user2", "content2", Instant.now());

        // Execution & Then
        assertEquals(tweet1, tweet2);
        assertEquals(tweet1.hashCode(), tweet2.hashCode());
    }

    @Test
    void testConstructor_shouldNotBeEqualWhenIdsDiffer() {
        // Configuring test
        Tweet tweet1 = new Tweet("id1", "user1", "content", Instant.now());
        Tweet tweet2 = new Tweet("id2", "user1", "content", Instant.now());

        // Execution & Then
        assertNotEquals(tweet1, tweet2);
    }
}
