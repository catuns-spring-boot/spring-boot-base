package xyz.catuns.spring.base.properties;

import java.time.Duration;

public class CacheProperties {
    private String name;
    private Duration ttl;

    public CacheProperties(String name, Duration ttl) {
        this.name = name;
        this.ttl = ttl;
    }

    public CacheProperties() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Duration getTtl() {
        return ttl;
    }

    public void setTtl(Duration ttl) {
        this.ttl = ttl;
    }
}
