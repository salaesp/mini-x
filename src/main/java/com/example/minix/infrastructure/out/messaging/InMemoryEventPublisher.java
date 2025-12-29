package com.example.minix.infrastructure.out.messaging;

import com.example.minix.application.handler.EventHandler;
import com.example.minix.application.port.out.EventPublisher;
import com.example.minix.domain.event.Event;
import com.example.minix.domain.event.EventType;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.example.minix.config.EventPublisherThreadpoolConfiguration.EVENT_PUBLISHER_POOL_EXECUTOR;

/**
 * In-memory implementation of EventPublisher.
 * Uses a thread pool for asynchronous event processing.
 * Simulates production-like behavior with async fan-out and backfill.
 */
@Component
@Slf4j
public class InMemoryEventPublisher implements EventPublisher {

    private final ExecutorService executorService;
    private final Map<EventType, List<EventHandler<?>>> handlers;


    // TODO Extract threadpool configuration to yml and probably a configuration file
    public InMemoryEventPublisher(@Qualifier(EVENT_PUBLISHER_POOL_EXECUTOR) ThreadPoolExecutor executorService) {
        this.executorService = executorService;
        handlers = new HashMap<>();
    }


    @Override
    @SuppressWarnings("unchecked")
    public <E extends Event> void publish(final E event) {
        if (!handlers.containsKey(event.getType())) {
            log.warn("No handler found for event {}", event.getType());
        }
        final var eventHandlers = handlers.get(event.getType());
        eventHandlers.forEach(handler -> executorService.submit(() -> {
            ((EventHandler<E>) handler).handle(event);
        }));
    }

    @Override
    public synchronized <E extends Event> void addListener(final EventType eventType,
                                                           final EventHandler<E> handler) {
        var interestedHandlers = handlers.computeIfAbsent(eventType, k -> new ArrayList<>());
        interestedHandlers.add(handler);
        log.info("Handler {} registered for type {}", handler.getClass().getCanonicalName(), eventType);
    }


    /**
     * Gracefully shutdown the executor on application shutdown.
     */
    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
