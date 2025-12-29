package com.example.minix.infrastructure.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for create tweet request.
 */
public record CreateTweetRequest(
        @NotBlank(message = "Tweet cannot be empty")
        @Size(max = 280, message = "Tweet cannot be longer than 280 chars")
        String content
) {
}