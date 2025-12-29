package com.example.minix.application.usecase;

import com.example.minix.application.port.out.EventPublisher;
import com.example.minix.application.port.out.TweetRepository;
import com.example.minix.domain.event.TweetCreatedEvent;
import com.example.minix.domain.model.Tweet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CreateTweetUseCase.
 */
@ExtendWith(MockitoExtension.class)
public class CreateTweetUseCaseTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private CreateTweetUseCase useCase;

    @Test
    public void testExecute_shouldCreateTweetSuccessfully() {
        // Configuring test
        String authorId = "user123";
        String content = "This is a test tweet";
        Tweet savedTweet = new Tweet("tweet123", authorId, content, Instant.now());

        when(tweetRepository.save(any(Tweet.class))).thenReturn(savedTweet);

        // Execution
        Tweet result = useCase.execute(authorId, content);

        // Verifications
        // tweet is saved
        ArgumentCaptor<Tweet> tweetCaptor = ArgumentCaptor.forClass(Tweet.class);
        verify(tweetRepository).save(tweetCaptor.capture());

        Tweet capturedTweet = tweetCaptor.getValue();
        assertEquals(authorId, capturedTweet.authorId());
        assertEquals(content, capturedTweet.content());

        //event is published
        ArgumentCaptor<TweetCreatedEvent> eventCaptor = ArgumentCaptor.forClass(TweetCreatedEvent.class);
        verify(eventPublisher).publish(eventCaptor.capture());

        TweetCreatedEvent capturedEvent = eventCaptor.getValue();
        assertEquals(savedTweet.id(), capturedEvent.tweetId());
        assertEquals(savedTweet.authorId(), capturedEvent.authorId());
        assertEquals(savedTweet.content(), capturedEvent.content());
        assertEquals(savedTweet.createdAt(), capturedEvent.createdAt());

        // Event is triggered after the save
        var inOrder = inOrder(tweetRepository, eventPublisher);
        inOrder.verify(tweetRepository).save(any(Tweet.class));
        inOrder.verify(eventPublisher).publish(any(TweetCreatedEvent.class));

        assertSame(savedTweet, result);

    }
}
