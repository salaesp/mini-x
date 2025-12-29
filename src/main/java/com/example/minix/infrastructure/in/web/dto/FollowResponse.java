package com.example.minix.infrastructure.in.web.dto;

import com.example.minix.domain.model.Follow;

/**
 * DTO for follow response.
 */
public record FollowResponse(
        String followerId,
        String followedId
) {
    public static FollowResponse fromDomain(Follow follow) {
        return new FollowResponse(
            follow.followerId(),
            follow.followedId()
        );
    }
}
