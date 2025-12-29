package com.example.minix.config;

import com.example.minix.application.port.out.EventPublisher;
import com.example.minix.domain.event.EventType;
import com.example.minix.application.handler.TweetCreatedEventHandler;
import com.example.minix.application.handler.UserFollowedEventHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    private final EventPublisher publisher;
    private final TweetCreatedEventHandler tweetCreatedEventHandler;
    private final UserFollowedEventHandler userFollowedEventHandler;

    public HandlerConfiguration(final EventPublisher publisher, final TweetCreatedEventHandler tweetCreatedEventHandler,
            final UserFollowedEventHandler userFollowedEventHandler) {
        this.publisher = publisher;
        this.tweetCreatedEventHandler = tweetCreatedEventHandler;
        this.userFollowedEventHandler = userFollowedEventHandler;
    }

    @PostConstruct
    public void registerHandlers() {
        publisher.addListener(EventType.TWEET_CREATED, tweetCreatedEventHandler);
        publisher.addListener(EventType.USER_FOLLOWED, userFollowedEventHandler);
    }
}
