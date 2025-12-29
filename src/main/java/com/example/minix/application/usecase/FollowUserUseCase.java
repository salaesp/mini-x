package com.example.minix.application.usecase;

import com.example.minix.application.port.out.EventPublisher;
import com.example.minix.application.port.out.FollowRepository;
import com.example.minix.domain.event.UserFollowedEvent;
import com.example.minix.domain.model.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Use case for following a user.
 */
@Component
@RequiredArgsConstructor
public class FollowUserUseCase {
    
    private final FollowRepository followRepository;
    private final EventPublisher eventPublisher;

    /**
     * Executes the follow user use case.
     * Publishes UserFollowedEvent for timeline backfill.
     *
     * @param followerId the follower ID
     * @param followedId the followed user ID
     * @return the created follow relationship
     */
    public Follow execute(String followerId, String followedId) {
        Follow follow = new Follow(followerId, followedId);
        Follow savedFollow = followRepository.save(follow);
        
        UserFollowedEvent event = new UserFollowedEvent(followerId, followedId);
        eventPublisher.publish(event);
        
        return savedFollow;
    }
}
