package com.example.minix.application.port.out;

import com.example.minix.application.handler.EventHandler;
import com.example.minix.domain.event.Event;
import com.example.minix.domain.event.EventType;

/**
 * Port interface for publishing domain events.
 * Defines the contract for event publishing and handler registration.
 */
public interface EventPublisher {
    
    /**
     * Publishes an event to all registered handlers.
     *
     * @param event the event to publish
     * @param <E> the event type
     */
    <E extends Event> void publish(E event);
    
    /**
     * Registers an event handler for a specific event type.
     *
     * @param eventType the type of event to listen for
     * @param handler the handler to register
     * @param <E> the event type
     */
    <E extends Event> void addListener(EventType eventType, EventHandler<E> handler);
}
