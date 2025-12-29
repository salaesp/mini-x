package com.example.minix.application.handler;

import com.example.minix.domain.event.Event;

/**
 * Port interface for handling domain events.
 * Defines the contract for event handlers.
 *
 * @param <E> the type of event this handler processes
 */
public interface EventHandler<E extends Event> {
    
    /**
     * Handles the given event.
     *
     * @param event the event to handle
     */
    void handle(E event);
}
