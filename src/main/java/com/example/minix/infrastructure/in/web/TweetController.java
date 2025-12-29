package com.example.minix.infrastructure.in.web;

import com.example.minix.application.usecase.CreateTweetUseCase;
import com.example.minix.application.usecase.FollowUserUseCase;
import com.example.minix.domain.model.Follow;
import com.example.minix.domain.model.Tweet;
import com.example.minix.infrastructure.in.web.dto.CreateTweetRequest;
import com.example.minix.infrastructure.in.web.dto.ErrorResponse;
import com.example.minix.infrastructure.in.web.dto.FollowRequest;
import com.example.minix.infrastructure.in.web.dto.FollowResponse;
import com.example.minix.infrastructure.in.web.dto.TweetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
 * REST controller for tweet operations.
 */
@RestController
@RequestMapping("/tweets")
public class TweetController {

    private final CreateTweetUseCase createTweetUseCase;

    public TweetController(CreateTweetUseCase createTweetUseCase) {
        this.createTweetUseCase = createTweetUseCase;
    }

    @PostMapping
    public ResponseEntity<TweetResponse> createTweet(
            @RequestHeader(USER_ID_HEADER) String userId,
            @RequestBody @Valid CreateTweetRequest request) {

        Tweet tweet = createTweetUseCase.execute(userId, request.content());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TweetResponse.fromDomain(tweet));
    }
}
