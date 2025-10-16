package xyz.catuns.spring.base.constraint.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for constraint violation handling.
 * <p>
 * This allows customization of how database constraint violations are processed
 * and returned to clients as RFC 7807 Problem Details.
 *
 * @author Devin Catuns
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "app.exception.constraint")
public class ConstraintViolationProperties {

    /**
     * Enable or disable the constraint violation handler.
     * <p>
     * When disabled, no constraint violation beans will be registered and
     * exceptions will fall back to default Spring Boot error handling.
     * <p>
     * Default: {@code true}
     */
    private boolean enabled = true;

    /**
     * Expose technical details in problem detail responses.
     * <p>
     * This includes raw SQL error messages and constraint names. Should only
     * be enabled in development environments for debugging purposes as it may
     * expose sensitive database schema information.
     * <p>
     * Default: {@code false}
     *
     * @deprecated Use logging instead of exposing technical details to clients.
     * This will be removed in version 2.0.0.
     */
    @Deprecated(since = "0.0.", forRemoval = true)
    private boolean exposeTechnicalDetails = false;

    /**
     * Include rejected values in the response.
     * <p>
     * When enabled, the actual value that caused the constraint violation will
     * be included in the problem detail. Be cautious with sensitive data.
     * <p>
     * Default: {@code false}
     */
    private boolean includeRejectedValues = false;

    /**
     * HTTP status code to use for constraint violations.
     * <p>
     * Common values:
     * <ul>
     *   <li>400 (Bad Request) - Generic client error</li>
     *   <li>409 (Conflict) - Recommended for constraint violations</li>
     *   <li>422 (Unprocessable Entity) - Semantic errors</li>
     * </ul>
     * <p>
     * Default: {@code 409}
     */
    private int statusCode = 409;

    /**
     * Strategy for formatting field names in error messages.
     * <p>
     * Default: {@link FieldNameStrategy#TITLE_CASE}
     */
    private FieldNameStrategy fieldNameStrategy = FieldNameStrategy.TITLE_CASE;

    /**
     * Field name transformation strategy.
     */
    public enum FieldNameStrategy {
        /**
         * Convert snake_case to Title Case.
         * <p>
         * Example: {@code user_email} becomes {@code User Email}
         */
        TITLE_CASE,

        /**
         * Convert snake_case to camelCase.
         * <p>
         * Example: {@code user_email} becomes {@code userEmail}
         */
        CAMEL_CASE,

        /**
         * Keep original field name as-is.
         * <p>
         * Example: {@code user_email} stays {@code user_email}
         */
        ORIGINAL
    }


}