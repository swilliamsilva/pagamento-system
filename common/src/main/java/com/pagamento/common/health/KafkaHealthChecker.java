package com.pagamento.common.health;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Component
public class KafkaHealthChecker implements DependencyHealthChecker {

    private final AdminClient adminClient;
    private final Set<String> requiredTopics;
    
    public KafkaHealthChecker(
        AdminClient adminClient,
        @Value("${kafka.required-topics:}") String[] requiredTopics
    ) {
        this.adminClient = adminClient;
        this.requiredTopics = requiredTopics.length == 0 ? 
            Collections.emptySet() : 
            Set.of(requiredTopics);
            /* The method of(String[]) is undefined for the type Set*/
    }
    
    @Override
    public Health checkHealth() {
        Instant start = Instant.now();
        try {
            ListTopicsResult topicsResult = adminClient.listTopics(new ListTopicsOptions().timeoutMs(3000));
            Set<String> topics = topicsResult.names().get(2, TimeUnit.SECONDS);
            
            Duration duration = Duration.between(start, Instant.now());
            Builder builder = Health.up()
                .withDetail("response_time_ms", duration.toMillis())
                .withDetail("topics_found", topics.size());
            /* Cannot invoke withDetail(String, int) on the primitive type void*/
            if (!requiredTopics.isEmpty()) {
                Set<String> missingTopics = requiredTopics.stream()
                    .filter(topic -> !topics.contains(topic))
                    .collect(Collectors.toSet());
                
                if (!missingTopics.isEmpty()) {
                    builder = Health.down()
                    		/*Cannot invoke withDetail(String, Set<String>) on the primitive type void */
                        .withDetail("error", "Missing required topics")
                        .withDetail("missing_topics", missingTopics);
                }
            }
            
            return builder.build();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Health.down()
            		/* Cannot invoke withDetail(String, long) on the primitive type void */
                .withDetail("error", "Interrupted while checking Kafka health")
                .withDetail("response_time_ms", Duration.between(start, Instant.now()).toMillis())
                .build();
        } catch (ExecutionException | TimeoutException e) {
            return Health.down()
            		/* Cannot invoke withDetail(String, long) on the primitive type void*/
                .withDetail("error", e.getMessage())
                .withDetail("response_time_ms", Duration.between(start, Instant.now()).toMillis())
                .build();
        }
    }
}