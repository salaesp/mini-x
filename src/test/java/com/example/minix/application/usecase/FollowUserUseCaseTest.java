package com.example.minix.application.usecase;

import com.example.minix.application.port.out.EventPublisher;
import com.example.minix.application.port.out.FollowRepository;
import com.example.minix.domain.event.UserFollowedEvent;
import com.example.minix.domain.model.Follow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FollowUserUseCase.
 */
@ExtendWith(MockitoExtension.class)
public class FollowUserUseCaseTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private FollowUserUseCase useCase;


    @Test
    public void testExecute_shouldCreateFollowRelationshipSuccessfully() {
        // Configuring test
        String followerId = "user1";
        String followedId = "user2";
        Follow savedFollow = new Follow(followerId, followedId);

        when(followRepository.save(any(Follow.class))).thenReturn(savedFollow);

        // Execution
        Follow result = useCase.execute(followerId, followedId);

        // Save is called before publishing the event
        var inOrder = inOrder(followRepository, eventPublisher);
        inOrder.verify(followRepository).save(any(Follow.class));
        inOrder.verify(eventPublisher).publish(any(UserFollowedEvent.class));

        // Follower is saved
        ArgumentCaptor<Follow> followCaptor = ArgumentCaptor.forClass(Follow.class);
        verify(followRepository).save(followCaptor.capture());

        Follow capturedFollow = followCaptor.getValue();
        assertEquals(followerId, capturedFollow.followerId());
        assertEquals(followedId, capturedFollow.followedId());

        // Event is published
        ArgumentCaptor<UserFollowedEvent> eventCaptor = ArgumentCaptor.forClass(UserFollowedEvent.class);
        verify(eventPublisher).publish(eventCaptor.capture());

        UserFollowedEvent capturedEvent = eventCaptor.getValue();
        assertEquals(followerId, capturedEvent.followerId());
        assertEquals(followedId, capturedEvent.followedId());

        // Result is correctly returned
        assertSame(savedFollow, result);
    }

    @Test
    public void testExecute_shouldAllowSelfFollow() {
        // Configuring test
        String userId = "user1";
        Follow savedFollow = new Follow(userId, userId);

        when(followRepository.save(any(Follow.class))).thenReturn(savedFollow);

        // Execution
        Follow result = useCase.execute(userId, userId);

        // Verifications
        assertNotNull(result);
        assertEquals(userId, result.followerId());
        assertEquals(userId, result.followedId());

        verify(followRepository).save(any(Follow.class));
        verify(eventPublisher).publish(any(UserFollowedEvent.class));
    }
}
