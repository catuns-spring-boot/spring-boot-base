package xyz.catuns.spring.base.autoconfigure.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.catuns.spring.base.properties.ExceptionHandlerMetadata;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.exeption")
public class ExceptionHandlerProperties extends ExceptionHandlerMetadata {
    /**
     * Enable or disable global exception handler
     *
     */
    private boolean enabled = true;

}
