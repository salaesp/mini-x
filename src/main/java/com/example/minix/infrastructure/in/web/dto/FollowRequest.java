package com.example.minix.infrastructure.in.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for follow request.
 */
public record FollowRequest(
        @NotBlank(message = "User follow id required")
        String followedId
) {
}
