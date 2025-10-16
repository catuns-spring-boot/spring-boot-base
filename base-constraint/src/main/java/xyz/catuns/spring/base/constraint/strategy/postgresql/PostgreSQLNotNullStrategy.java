package xyz.catuns.spring.base.constraint.strategy.postgresql;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * PostgreSQL: null value in column "email" violates not-null constraint
 * */
public class PostgreSQLNotNullStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("null value in column") &&
                message.contains("violates not-null constraint");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String field = extractBetween(message, "column \"", "\"");

        String userMessage = field != null
                ? String.format("Field '%s' is required and cannot be empty", formatFieldName(field))
                : "Required field is missing";

        return new ConstraintViolationInfo(
                "NOT_NULL_VIOLATION",
                null,
                field,
                null,
                userMessage
        );
    }
}
