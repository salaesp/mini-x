package com.example.minix.application.usecase;

import com.example.minix.application.port.out.TimelineRepository;
import com.example.minix.domain.model.TimelineItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Use case for retrieving a user's timeline.
 */

@Component
public class GetTimelineUseCase {

    private final TimelineRepository timelineRepository;
    private final int maxTimelineLength;

    public GetTimelineUseCase(final TimelineRepository timelineRepository,
                              @Value("${timeline.max-length}") final int maxTimelineLength) {
        this.timelineRepository = timelineRepository;
        this.maxTimelineLength = maxTimelineLength;
    }


    /**
     * Executes the get timeline use case.
     * Returns top 50 items from the user's pre-computed timeline.
     *
     * @param userId the user ID
     * @return list of timeline items (newest first)
     */
    public List<TimelineItem> execute(String userId) {
        return timelineRepository.getTimeline(userId, maxTimelineLength);
    }
}
