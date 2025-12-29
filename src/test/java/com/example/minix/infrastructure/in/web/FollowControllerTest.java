package com.example.minix.infrastructure.in.web;

import com.example.minix.application.usecase.CreateTweetUseCase;
import com.example.minix.application.usecase.FollowUserUseCase;
import com.example.minix.config.AuthFilterProperties;
import com.example.minix.domain.model.Follow;
import com.example.minix.domain.model.Tweet;
import com.example.minix.infrastructure.in.web.dto.CreateTweetRequest;
import com.example.minix.infrastructure.in.web.dto.FollowRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;

import static com.example.minix.infrastructure.in.web.ControllerAuthConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for TweetController.
 */
@WebMvcTest(FollowController.class)
@EnableConfigurationProperties(AuthFilterProperties.class)
class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FollowUserUseCase followUserUseCase;

    @Test
    void shouldFollowUserSuccessfully() throws Exception {
        // Configuring test
        String followerId = "user1";
        String followedId = "user2";
        FollowRequest request = new FollowRequest(followedId);
        Follow follow = new Follow(followerId, followedId);

        when(followUserUseCase.execute(followerId, followedId)).thenReturn(follow);

        // Execution 
        MvcResult result = mockMvc.perform(post("/follow")
                .header(USER_ID_HEADER, followerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        // validate
        Follow actual = objectMapper.readValue(result.getResponse().getContentAsString(), Follow.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(follow);

        verify(followUserUseCase, times(1)).execute(followerId, followedId);
    }

    @Test
    void shouldReturn400WhenFollowedIdIsNull() throws Exception {
        // Configuring test
        String followerId = "user1";
        FollowRequest request = new FollowRequest(null);

        // Execution 
        mockMvc.perform(post("/follow")
                .header(USER_ID_HEADER, followerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // validate
        verify(followUserUseCase, never()).execute(any(), any());
    }

    @Test
    void shouldReturn400WhenFollowedIdIsEmpty() throws Exception {
        // Configuring test
        String followerId = "user1";
        FollowRequest request = new FollowRequest("");

        // Execution 
        mockMvc.perform(post("/follow")
                .header(USER_ID_HEADER, followerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // validate
        verify(followUserUseCase, never()).execute(any(), any());
    }

    @Test
    void shouldAllowSelfFollow() throws Exception {
        // Configuring test
        String userId = "user1";
        FollowRequest request = new FollowRequest(userId);
        Follow follow = new Follow(userId, userId);

        when(followUserUseCase.execute(userId, userId)).thenReturn(follow);

        // Execution 
        MvcResult result = mockMvc.perform(post("/follow")
                .header(USER_ID_HEADER, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        // validate
        Follow actual = objectMapper.readValue(result.getResponse().getContentAsString(), Follow.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(follow);

        verify(followUserUseCase, times(1)).execute(userId, userId);
    }


    @Test
    void shouldReturn401WhenUserIdHeaderIsMissing() throws Exception {
        // Configuring test
        String userId = "user1";
        FollowRequest request = new FollowRequest(userId);

        // Execution 
        mockMvc.perform(post("/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        // validate
        verify(followUserUseCase, never()).execute(any(), any());
    }
}
