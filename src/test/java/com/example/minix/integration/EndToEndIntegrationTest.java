package com.example.minix.integration;

import com.example.minix.infrastructure.in.web.dto.CreateTweetRequest;
import com.example.minix.infrastructure.in.web.dto.FollowRequest;
import com.example.minix.infrastructure.in.web.dto.FollowResponse;
import com.example.minix.infrastructure.in.web.dto.TimelineItemResponse;
import com.example.minix.infrastructure.in.web.dto.TweetResponse;
import com.example.minix.infrastructure.in.web.dto.ErrorResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-end integration tests covering complete user workflows.
 * Tests the interaction between multiple endpoints and use cases.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EndToEndIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCompleteFullUserJourney() throws Exception {
        // Configuring test - Unique users for this test
        String alice = "alice_e2e_" + System.currentTimeMillis();
        String bob = "bob_e2e_" + System.currentTimeMillis();

        // Step 1: Alice creates a tweet
        CreateTweetRequest aliceTweet = new CreateTweetRequest("Hello from Alice!");
        String aliceTweetResponseStr = mockMvc.perform(post("/tweets")
                        .header("X-User-Id", alice)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aliceTweet)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        TweetResponse aliceTweetResponse = objectMapper.readValue(aliceTweetResponseStr, TweetResponse.class);
        assertEquals(alice, aliceTweetResponse.authorId());
        assertEquals("Hello from Alice!", aliceTweetResponse.content());

        // Step 2: Bob creates a tweet
        CreateTweetRequest bobTweet = new CreateTweetRequest("Hello from Bob!");
        String bobTweetResponseStr = mockMvc.perform(post("/tweets")
                        .header("X-User-Id", bob)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bobTweet)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        TweetResponse bobTweetResponse = objectMapper.readValue(bobTweetResponseStr, TweetResponse.class);
        assertEquals(bob, bobTweetResponse.authorId());
        assertEquals("Hello from Bob!", bobTweetResponse.content());

        // Step 2.5: Alice checks her timeline (should be empty before following Bob)
        String aliceTimelineBeforeFollowStr = mockMvc.perform(get("/timeline")
                        .header("X-User-Id", alice))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<TimelineItemResponse> aliceTimelineBeforeFollow = objectMapper.readValue(
                aliceTimelineBeforeFollowStr, new TypeReference<>() {
                });
        assertNotNull(aliceTimelineBeforeFollow);
        assertTrue(aliceTimelineBeforeFollow.isEmpty(),
                "Alice's timeline should be empty before following anyone");

        // Step 3: Alice follows Bob
        FollowRequest followRequest = new FollowRequest(bob);
        String followResponseStr = mockMvc.perform(post("/follow")
                        .header("X-User-Id", alice)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(followRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        FollowResponse followResponse = objectMapper.readValue(followResponseStr, FollowResponse.class);
        assertEquals(alice, followResponse.followerId());
        assertEquals(bob, followResponse.followedId());

        // Step 4: Alice checks her timeline (should contain Bob's tweet now)
        String aliceTimelineStr = mockMvc.perform(get("/timeline")
                        .header("X-User-Id", alice))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<TimelineItemResponse> aliceTimeline = objectMapper.readValue(aliceTimelineStr,
                new TypeReference<>() {
                });
        assertNotNull(aliceTimeline);
        assertEquals(1, aliceTimeline.size(), "Alice should see Bob's tweet after following him");
        assertEquals(bob, aliceTimeline.get(0).authorId());
        assertEquals("Hello from Bob!", aliceTimeline.get(0).content());

        // Step 5: Bob checks his timeline
        String bobTimelineStr = mockMvc.perform(get("/timeline")
                        .header("X-User-Id", bob))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<TimelineItemResponse> bobTimeline = objectMapper.readValue(bobTimelineStr,
                new TypeReference<>() {
                });
        assertNotNull(bobTimeline);
        assertTrue(bobTimeline.isEmpty(), "Bob's timeline should not have changed");

    }
}
