package com.example.spring_cloud_streams;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.function.Function;

@Configuration
public class DefaultConfiguration {

    @Bean
    public Function<String, String> enrichLogMessage() {
        return value -> "[%s] - %s".formatted("Marcus", value);
    }

    @Bean
    public Function<String, Message<String>> processLogs() {
        return log -> {
            boolean shouldBeEnriched = log.length() > 10;
            String destination = shouldBeEnriched ? "enrichLogMessage-in-0" : "my.output.queue.log.messages";
            return MessageBuilder.withPayload(log)
                    .setHeader("spring.cloud.stream.sendto.destination", destination)
                    .build();
        };
    }
}
