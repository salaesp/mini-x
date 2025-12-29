package com.example.minix.infrastructure.in.web;

import com.example.minix.application.usecase.CreateTweetUseCase;
import com.example.minix.application.usecase.FollowUserUseCase;
import com.example.minix.domain.model.Follow;
import com.example.minix.domain.model.Tweet;
import com.example.minix.infrastructure.in.web.dto.CreateTweetRequest;
import com.example.minix.infrastructure.in.web.dto.FollowRequest;
import com.example.minix.infrastructure.in.web.dto.FollowResponse;
import com.example.minix.infrastructure.in.web.dto.TweetResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.minix.infrastructure.in.web.ControllerAuthConstants.USER_ID_HEADER;

/**
 * REST controller for follow operations.
 */
@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowUserUseCase followUserUseCase;

    public FollowController(FollowUserUseCase followUserUseCase) {
        this.followUserUseCase = followUserUseCase;
    }

    @PostMapping
    public ResponseEntity<FollowResponse> followUser(
            @RequestHeader(USER_ID_HEADER) String userId,
            @RequestBody @Valid FollowRequest request) {

        Follow follow = followUserUseCase.execute(userId, request.followedId());
        return ResponseEntity.ok(FollowResponse.fromDomain(follow));
    }
}
