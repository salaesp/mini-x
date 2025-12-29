package com.example.minix.infrastructure.in.web;

import com.example.minix.application.usecase.GetTimelineUseCase;
import com.example.minix.domain.model.TimelineItem;
import com.example.minix.infrastructure.in.web.dto.TimelineItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.minix.infrastructure.in.web.ControllerAuthConstants.USER_ID_HEADER;

/**
 * REST controller for timeline operations.
 */
@RestController
@RequestMapping("/timeline")
public class TimelineController {
    
    private final GetTimelineUseCase getTimelineUseCase;
    
    public TimelineController(GetTimelineUseCase getTimelineUseCase) {
        this.getTimelineUseCase = getTimelineUseCase;
    }
    
    @GetMapping
    public ResponseEntity<List<TimelineItemResponse>> getTimeline(
            @RequestHeader(USER_ID_HEADER) String userId) {
        
        List<TimelineItem> timeline = getTimelineUseCase.execute(userId);
        
        List<TimelineItemResponse> response = timeline.stream()
                .map(TimelineItemResponse::fromDomain)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
}
