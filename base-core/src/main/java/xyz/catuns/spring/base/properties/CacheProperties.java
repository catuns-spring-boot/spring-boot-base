package xyz.catuns.spring.base.properties;

import lombok.Data;

import java.time.Duration;

@Data
public class CacheProperties {
    /*
     * Cache Name
     */
    private String name;

    /*
     * Cache Time to live
     */
    private Duration ttl;
}
