package com.example.minix.infrastructure.out.persistence.inmemory;

import com.example.minix.application.port.out.TweetRepository;
import com.example.minix.domain.model.Tweet;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of TweetRepository.
 * Thread-safe using ConcurrentHashMap.
 */
@Repository
public class InMemoryTweetRepository implements TweetRepository {

    private final ConcurrentHashMap<String, Tweet> tweets = new ConcurrentHashMap<>();

    @Override
    public Tweet save(Tweet tweet) {
        tweets.put(tweet.id(), tweet);
        return tweet;
    }

    @Override
    public Optional<Tweet> findById(String id) {
        return Optional.ofNullable(tweets.get(id));
    }

    @Override
    public List<Tweet> findByAuthorId(String authorId) {
        return tweets.values().stream()
                .filter(tweet -> tweet.authorId().equals(authorId))
                .sorted(Comparator.comparing(Tweet::createdAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Tweet> findByAuthorIdAfter(String authorId, int limit) {
        return tweets.values().stream()
                .filter(tweet -> tweet.authorId().equals(authorId))
                .sorted(Comparator.comparing(Tweet::createdAt).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tweet> findAll() {
        return tweets.values().stream()
                .sorted(Comparator.comparing(Tweet::createdAt).reversed())
                .collect(Collectors.toList());
    }
}
