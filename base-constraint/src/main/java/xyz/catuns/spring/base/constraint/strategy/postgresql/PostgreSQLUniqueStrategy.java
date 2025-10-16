package xyz.catuns.spring.base.constraint.strategy.postgresql;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * PostgreSQL: duplicate key value violates unique constraint "users_email_key"
 * Detail: Key (email)=(test@example.com) already exists.
 */
public class PostgreSQLUniqueStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("duplicate key value violates unique constraint");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String constraintName = extractBetween(message, "constraint \"", "\"");
        String field = extractBetween(message, "Key (", ")");
        String value = extractBetween(message, ")=(", ")");

        String userMessage = field != null
                ? String.format("A record with this %s already exists", formatFieldName(field))
                : "This record already exists in the database";

        return new ConstraintViolationInfo(
                "UNIQUE_VIOLATION",
                constraintName,
                field,
                value,
                userMessage
        );
    }
}

