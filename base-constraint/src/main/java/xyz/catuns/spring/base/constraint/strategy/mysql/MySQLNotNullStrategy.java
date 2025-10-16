package xyz.catuns.spring.base.constraint.strategy.mysql;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

public class MySQLNotNullStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("Column") && message.contains("cannot be null");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String field = extractBetween(message, "Column '", "'");

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
