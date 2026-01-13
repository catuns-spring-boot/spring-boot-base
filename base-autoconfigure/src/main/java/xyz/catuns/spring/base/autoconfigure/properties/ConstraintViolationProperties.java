package xyz.catuns.spring.base.autoconfigure.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.catuns.spring.base.constraint.properties.ConstraintViolationMetadata;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.exception.constraint")
public class ConstraintViolationProperties extends ConstraintViolationMetadata {
    /**
     * Enable or disable the constraint violation handler.
     * <p>
     * When disabled, no constraint violation beans will be registered and
     * exceptions will fall back to default Spring Boot error handling.
     * <p>
     * Default: {@code true}
     */
    private boolean enabled = true;
}
