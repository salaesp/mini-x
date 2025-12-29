package com.example.minix.application.usecase;

import com.example.minix.application.port.out.EventPublisher;
import com.example.minix.application.port.out.TweetRepository;
import com.example.minix.domain.event.TweetCreatedEvent;
import com.example.minix.domain.model.Tweet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Use case for creating a tweet.
 * Orchestrates tweet creation and event publishing.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CreateTweetUseCase {

    private final TweetRepository tweetRepository;
    private final EventPublisher eventPublisher;

    /**
     * Executes the creation tweet use case.
     * Validates that the user exists before creating the tweet.
     *
     * @param authorId the author ID
     * @param content the tweet content
     * @return the created tweet
     * @throws IllegalArgumentException if user does not exist
     */
    public Tweet execute(String authorId, String content) {
        Tweet tweet = new Tweet(authorId, content);
        
        Tweet savedTweet = tweetRepository.save(tweet);
        
        TweetCreatedEvent event = new TweetCreatedEvent(
            savedTweet.id(),
            savedTweet.authorId(),
            savedTweet.content(),
            savedTweet.createdAt()
        );

        eventPublisher.publish(event);
        
        return savedTweet;
    }
}
