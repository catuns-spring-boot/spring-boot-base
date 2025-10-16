package xyz.catuns.spring.base.constraint.strategy.h2;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * H2: NULL not allowed for column "EMAIL"
 */
public class H2NotNullStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("NULL not allowed for column");
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
