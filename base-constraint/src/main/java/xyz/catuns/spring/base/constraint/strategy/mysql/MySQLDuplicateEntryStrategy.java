package xyz.catuns.spring.base.constraint.strategy.mysql;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * MySQL: Duplicate entry 'test@example.com' for key 'users.email'
 */
public class MySQLDuplicateEntryStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("Duplicate entry") && message.contains("for key");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String value = extractBetween(message, "Duplicate entry '", "'");
        String key = extractBetween(message, "for key '", "'");

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

