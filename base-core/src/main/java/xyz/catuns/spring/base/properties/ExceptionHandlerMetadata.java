package xyz.catuns.spring.base.properties;

import lombok.Data;

/**
 * Configuration properties for global exception handling.
 * <p>
 *
 * @author Devin Catuns
 * @since 1.0.0
 */
@Data
public class ExceptionHandlerMetadata {


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
