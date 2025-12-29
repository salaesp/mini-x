package com.example.minix.infrastructure.in.web;

import com.example.minix.application.usecase.CreateTweetUseCase;
import com.example.minix.config.AuthFilterProperties;
import com.example.minix.domain.model.Tweet;
import com.example.minix.infrastructure.in.web.dto.CreateTweetRequest;
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
@WebMvcTest(TweetController.class)
@EnableConfigurationProperties(AuthFilterProperties.class)
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateTweetUseCase createTweetUseCase;

    @Test
    void shouldCreateTweetSuccessfully() throws Exception {
        // Configuring test
        String userId = "user123";
        String content = "This is a test tweet";
        CreateTweetRequest request = new CreateTweetRequest(content);
        Tweet tweet = new Tweet("tweet123", userId, content, Instant.now());

        when(createTweetUseCase.execute(userId, content)).thenReturn(tweet);

        // Execution
        MvcResult result = mockMvc.perform(post("/tweets")
                .header(USER_ID_HEADER, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        // validate
        Tweet actual = objectMapper.readValue(result.getResponse().getContentAsString(), Tweet.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(tweet);

        verify(createTweetUseCase).execute(userId, content);
    }

    @Test
    void shouldReturn400WhenContentIsEmpty() throws Exception {
        // Configuring test
        String userId = "user123";
        CreateTweetRequest request = new CreateTweetRequest("");

        // Execution
        mockMvc.perform(post("/tweets")
                .header(USER_ID_HEADER, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // validate
        verify(createTweetUseCase, never()).execute(any(), any());
    }

    @Test
    void shouldReturn400WhenContentIsNull() throws Exception {
        // Configuring test
        String userId = "user123";
        CreateTweetRequest request = new CreateTweetRequest(null);

        // Execution
        mockMvc.perform(post("/tweets")
                .header(USER_ID_HEADER, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(createTweetUseCase, never()).execute(any(), any());
    }

    @Test
    void shouldReturn400WhenContentExceedsMaxLength() throws Exception {
        // Configuring test
        String userId = "user123";
        String longContent = "a".repeat(281);
        CreateTweetRequest request = new CreateTweetRequest(longContent);

        // Execution
        mockMvc.perform(post("/tweets")
                .header(USER_ID_HEADER, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // validate
        verify(createTweetUseCase, never()).execute(any(), any());
    }

    @Test
    void shouldAcceptMaxLengthContent() throws Exception {
        // Configuring test
        String userId = "user123";
        String maxContent = "a".repeat(280);
        CreateTweetRequest request = new CreateTweetRequest(maxContent);
        Tweet tweet = new Tweet("tweet123", userId, maxContent, Instant.now());

        when(createTweetUseCase.execute(userId, maxContent)).thenReturn(tweet);

        // Execution
        MvcResult result = mockMvc.perform(post("/tweets")
                .header(USER_ID_HEADER, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        // validate
        Tweet actual = objectMapper.readValue(result.getResponse().getContentAsString(), Tweet.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(tweet);

        verify(createTweetUseCase, times(1)).execute(userId, maxContent);
    }

    @Test
    void shouldReturn401WhenUserIdHeaderIsMissing() throws Exception {
        // Configuring test
        CreateTweetRequest request = new CreateTweetRequest("Test content");

        // Execution
        mockMvc.perform(post("/tweets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        // validate
        verify(createTweetUseCase, never()).execute(any(), any());
    }

    @Test
    void shouldHandleUseCaseException() throws Exception {
        // Configuring test
        String userId = "user123";
        String content = "Test content";
        CreateTweetRequest request = new CreateTweetRequest(content);

        when(createTweetUseCase.execute(userId, content))
                .thenThrow(new IllegalArgumentException("Invalid tweet"));

        // Execution
        mockMvc.perform(post("/tweets")
                .header(USER_ID_HEADER, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // validate
        verify(createTweetUseCase, times(1)).execute(userId, content);
    }
}
