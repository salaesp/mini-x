package com.example.minix.domain.event;

/**
 * Domain event representing a user following another user.
 */
public record UserFollowedEvent(String followerId, String followedId) implements Event {
    @Override
    public EventType getType() {
        return EventType.USER_FOLLOWED;
    }
}
