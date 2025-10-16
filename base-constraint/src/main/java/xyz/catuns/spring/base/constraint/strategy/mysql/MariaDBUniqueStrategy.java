package xyz.catuns.spring.base.constraint.strategy.mysql;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * MariaDB has slightly different error format in some versions
 */
public class MariaDBUniqueStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("Duplicate entry") &&
                (message.contains("for key") || message.contains("for constraint"));
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        // Similar to MySQL but handles both "for key" and "for constraint"
        String value = extractBetween(message, "Duplicate entry '", "'");
        String key = extractBetween(message, "for key '", "'");

        if (key == null) {
            key = extractBetween(message, "for constraint '", "'");
        }

        String field = key != null && key.contains(".")
                ? key.substring(key.lastIndexOf(".") + 1)
                : key;

        String userMessage = field != null
                ? String.format("A record with this %s already exists", formatFieldName(field))
                : "This record already exists";

        return new ConstraintViolationInfo(
                "UNIQUE_VIOLATION",
                key,
                field,
                value,
                userMessage
        );
    }
}
