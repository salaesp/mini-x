package com.example.minix.application.port.out;

import com.example.minix.domain.model.Tweet;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Port interface for tweet persistence operations.
 * Defines the contract for storing and retrieving tweets.
 */
public interface TweetRepository {
    
    /**
     * Saves a tweet.
     *
     * @param tweet the tweet to save
     * @return the saved tweet
     */
    Tweet save(Tweet tweet);
    
    /**
     * Finds a tweet by its ID.
     *
     * @param id the tweet ID
     * @return an Optional containing the tweet if found
     */
    Optional<Tweet> findById(String id);
    
    /**
     * Finds all tweets by an author.
     *
     * @param authorId the author ID
     * @return list of tweets by the author
     */
    List<Tweet> findByAuthorId(String authorId);
    
    /**
     * Finds tweets by author.
     *
     * @param authorId the author ID
     * @param limit maximum number of tweets to return
     * @return list of tweets.
     */
    List<Tweet> findByAuthorIdAfter(String authorId, int limit);

    /**
     * Finds all tweets.
     *
     * @return list of all tweets
     */
    List<Tweet> findAll();
}
