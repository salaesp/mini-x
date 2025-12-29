package com.example.minix.infrastructure.out.persistence.inmemory;

import com.example.minix.application.port.out.FollowRepository;
import com.example.minix.domain.model.Follow;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of FollowRepository.
 * Thread-safe using ConcurrentHashMap and synchronized sets.
 */
@Repository
public class InMemoryFollowRepository implements FollowRepository {

    // Map: followed user ID -> set of follower IDs
    private final ConcurrentHashMap<String, Set<String>> followers = new ConcurrentHashMap<>();

    // Map: follower ID -> set of followed user IDs
    private final ConcurrentHashMap<String, Set<String>> following = new ConcurrentHashMap<>();
    
    @Override
    public Follow save(Follow follow) {
        // Add to followers map (who follows this user)
        followers.computeIfAbsent(follow.followedId(), k ->
            Collections.newSetFromMap(new ConcurrentHashMap<>()))
            .add(follow.followerId());
        
        // Add to following map (who this user follows)
        following.computeIfAbsent(follow.followerId(), k ->
            Collections.newSetFromMap(new ConcurrentHashMap<>()))
            .add(follow.followedId());
        
        return follow;
    }
    
    @Override
    public List<String> findFollowedUserIds(String followerId) {
        Set<String> followedSet = following.get(followerId);
        return followedSet != null ? new ArrayList<>(followedSet) : Collections.emptyList();
    }
    
    @Override
    public List<String> findFollowerIds(String followedId) {
        Set<String> followerSet = followers.get(followedId);
        return followerSet != null ? new ArrayList<>(followerSet) : Collections.emptyList();
    }
    
    @Override
    public List<String> getFollowers(String userId) {
        return findFollowerIds(userId);
    }
    
    @Override
    public boolean exists(String followerId, String followedId) {
        Set<String> followedSet = following.get(followerId);
        return followedSet != null && followedSet.contains(followedId);
    }
}
