package com.example.minix.infrastructure.in.web;

import com.example.minix.application.usecase.GetTimelineUseCase;
import com.example.minix.config.AuthFilterProperties;
import com.example.minix.domain.model.TimelineItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.minix.infrastructure.in.web.ControllerAuthConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for TimelineController.
 */
@WebMvcTest(TimelineController.class)
@EnableConfigurationProperties(AuthFilterProperties.class)
class TimelineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GetTimelineUseCase getTimelineUseCase;

    @Test
    void shouldGetTimelineSuccessfully() throws Exception {
        // Configuring test
        String userId = "user123";
        Instant now = Instant.now();
        List<TimelineItem> timeline = Arrays.asList(
                new TimelineItem("tweet1", "author1", "content1", now),
                new TimelineItem("tweet2", "author2", "content2", now.minusSeconds(60)),
                new TimelineItem("tweet3", "author3", "content3", now.minusSeconds(120)));

        when(getTimelineUseCase.execute(userId)).thenReturn(timeline);

        // Execution
        MvcResult result = mockMvc.perform(get("/timeline")
                .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andReturn();

        // validations
        List<TimelineItem> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        Assertions.assertEquals(timeline, actual);

        verify(getTimelineUseCase).execute(userId);
    }

    @Test
    void shouldReturnEmptyArrayWhenTimelineIsEmpty() throws Exception {
        // Configuring test
        String userId = "user123";
        when(getTimelineUseCase.execute(userId)).thenReturn(Collections.emptyList());

        // Execution 
        MvcResult result = mockMvc.perform(get("/timeline")
                .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andReturn();

        // validations
        List<TimelineItem> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertThat(actual).isEmpty();

        verify(getTimelineUseCase).execute(userId);
    }

    @Test
    void shouldReturn401WhenUserIdHeaderIsMissing() throws Exception {
        // Execution
        mockMvc.perform(get("/timeline"))
                .andExpect(status().isUnauthorized());

        // validations
        verify(getTimelineUseCase, never()).execute(any());
    }
}
