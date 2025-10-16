package xyz.catuns.spring.base.properties;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka broker properties
 * @author Devin Catuns
 * @since 1.0.0
 */
public class KafkaTopicProperties {
    /**
     * Map key to value topic name
     */
    private Map<String, String> topics = new HashMap<>();
    /**
     * Additional configuration properties to
     * apply to NewTopic builder
     */
    private Map<String, String> configs = new HashMap<>();
    /**
     * Number of partitions to apply to new topics
     */
    private Integer partitions = 1;
    /**
     * Number of replicas to apply to new topics
     */
    private Integer replicas = 1;
}
