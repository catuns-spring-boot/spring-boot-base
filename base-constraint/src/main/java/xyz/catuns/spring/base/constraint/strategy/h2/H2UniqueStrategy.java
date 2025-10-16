package xyz.catuns.spring.base.constraint.strategy.h2;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * H2: Unique index or primary key violation: "PUBLIC.UK_USERS_EMAIL ON PUBLIC.USERS(EMAIL) VALUES ('test@example.com', 1)"
 */
public class H2UniqueStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("Unique index or primary key violation");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String constraint = extractBetween(message, "\"", "\"");
        String field = null;

        if (constraint != null && constraint.contains("(")) {
            field = extractBetween(constraint, "(", ")");
        }

        String userMessage = field != null
                ? String.format("A record with this %s already exists", formatFieldName(field))
                : "This record already exists";

        return new ConstraintViolationInfo(
                "UNIQUE_VIOLATION",
                constraint,
                field,
                null,
                userMessage
        );
    }
}


