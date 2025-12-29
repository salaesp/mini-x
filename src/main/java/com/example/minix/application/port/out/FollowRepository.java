package com.example.minix.application.port.out;

import com.example.minix.domain.model.Follow;

import java.util.List;

/**
 * Port interface for follow relationship persistence operations.
 * Defines the contract for storing and retrieving follow relationships.
 */
public interface FollowRepository {
    
    /**
     * Saves a follow relationship.
     *
     * @param follow the follow relationship to save
     * @return the saved follow relationship
     */
    Follow save(Follow follow);
    
    /**
     * Finds all users that a given user is following.
     *
     * @param followerId the follower ID
     * @return list of followed user IDs
     */
    List<String> findFollowedUserIds(String followerId);
    
    /**
     * Finds all followers of a given user.
     *
     * @param followedId the followed user ID
     * @return list of follower IDs
     */
    List<String> findFollowerIds(String followedId);
    
    /**
     * Gets all followers of a given user (alias for findFollowerIds).
     *
     * @param userId the user ID
     * @return list of follower IDs
     */
    List<String> getFollowers(String userId);
    
    /**
     * Checks if a follow relationship exists.
     *
     * @param followerId the follower ID
     * @param followedId the followed user ID
     * @return true if the relationship exists
     */
    boolean exists(String followerId, String followedId);
}
