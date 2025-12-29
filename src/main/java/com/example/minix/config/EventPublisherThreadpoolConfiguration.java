package com.example.minix.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class EventPublisherThreadpoolConfiguration {

    public static final String EVENT_PUBLISHER_POOL_EXECUTOR = "EVENT_PUBLISHER_POOL_EXECUTOR";

    @Bean(EVENT_PUBLISHER_POOL_EXECUTOR)
    public ThreadPoolExecutor threadPoolExecutor(@Value("${event-publisher.threadpool.core-size}") int corePoolSize,
                                                 @Value("${event-publisher.threadpool.max-size}") int maxPoolSize,
                                                 @Value("${event-publisher.threadpool.ttl-in-sec}") int keepAliveTime,
                                                 @Value("${event-publisher.threadpool.queue-capacity}") int queueCapacity){
        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity)
        );
    }
}
