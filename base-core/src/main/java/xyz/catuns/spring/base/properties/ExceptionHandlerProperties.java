package xyz.catuns.spring.base.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for global exception handling.
 * <p>
 *
 * @author Devin Catuns
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "starter.base.exception")
public class ExceptionHandlerProperties {

    /**
     * Enable or disable global exception handler
     *
     */
    private boolean enabled = true;

    /**
     * Include stack trace in error responses (dev only)
     */
    private boolean includeStackTrace = false;

    /**
     * Include exception cause in error responses
     */
    private boolean includeCause = false;

    /**
     * Include binding errors details
     */
    private boolean includeBindingErrors = true;

    /**
     * Log all exceptions
     */
    private boolean logExceptions = true;
}
