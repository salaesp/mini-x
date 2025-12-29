package com.example.minix.domain.model;

import org.springframework.util.Assert;

/**
 * Domain model representing a follow relationship.
 */
public record Follow(String followerId, String followedId) {

    public Follow {
        Assert.hasText(followerId, "Follower ID cannot be empty");
        Assert.hasText(followedId, "Followed ID cannot be empty");
    }
}