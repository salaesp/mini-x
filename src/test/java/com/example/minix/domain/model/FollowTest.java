package com.example.minix.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Follow domain model.
 */
class FollowTest {

    @Test
    public void testConstructor_shouldCreateFollowWithValidIds() {
        // Configuring test
        String followerId = "user1";
        String followedId = "user2";

        // Execution
        Follow follow = new Follow(followerId, followedId);

        // Verifications
        assertEquals(followerId, follow.followerId());
        assertEquals(followedId, follow.followedId());
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenFollowerIdIsNull() {
        // Configuring test
        String followerId = null;
        String followedId = "user2";

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Follow(followerId, followedId)
        );
        assertTrue(exception.getMessage().contains("Follower ID cannot be empty"));
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenFollowerIdIsEmpty() {
        // Configuring test
        String followerId = "";
        String followedId = "user2";

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Follow(followerId, followedId)
        );
        assertTrue(exception.getMessage().contains("Follower ID cannot be empty"));
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenFollowerIdIsBlank() {
        // Configuring test
        String followerId = "   ";
        String followedId = "user2";

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Follow(followerId, followedId)
        );
        assertTrue(exception.getMessage().contains("Follower ID cannot be empty"));
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenFollowedIdIsNull() {
        // Configuring test
        String followerId = "user1";
        String followedId = null;

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Follow(followerId, followedId)
        );
        assertTrue(exception.getMessage().contains("Followed ID cannot be empty"));
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenFollowedIdIsEmpty() {
        // Configuring test
        String followerId = "user1";
        String followedId = "";

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Follow(followerId, followedId)
        );
        assertTrue(exception.getMessage().contains("Followed ID cannot be empty"));
    }

    @Test
    public void testConstructor_shouldThrowExceptionWhenFollowedIdIsBlank() {
        // Configuring test
        String followerId = "user1";
        String followedId = "   ";

        // Execution & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Follow(followerId, followedId)
        );
        assertTrue(exception.getMessage().contains("Followed ID cannot be empty"));
    }

    @Test
    public void testConstructor_shouldAllowSelfFollow() {
        // Configuring test
        String userId = "user1";

        // Execution
        Follow follow = new Follow(userId, userId);

        // Verifications
        assertEquals(userId, follow.followerId());
        assertEquals(userId, follow.followedId());
    }

    @Test
    public void testConstructor_shouldBeEqualWhenBothIdsMatch() {
        // Configuring test
        Follow follow1 = new Follow("user1", "user2");
        Follow follow2 = new Follow("user1", "user2");

        // Execution & Then
        assertEquals(follow1, follow2);
    }

    @Test
    public void testConstructor_shouldNotBeEqualWhenFollowerIdDiffers() {
        // Configuring test
        Follow follow1 = new Follow("user1", "user2");
        Follow follow2 = new Follow("user3", "user2");

        // Execution & Then
        assertNotEquals(follow1, follow2);
    }

    @Test
    public void testConstructor_shouldNotBeEqualWhenFollowedIdDiffers() {
        // Configuring test
        Follow follow1 = new Follow("user1", "user2");
        Follow follow2 = new Follow("user1", "user3");

        // Execution & Then
        assertNotEquals(follow1, follow2);
    }
}
