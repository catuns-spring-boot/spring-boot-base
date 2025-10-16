package xyz.catuns.spring.base.constraint.strategy.oracle;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * Oracle: ORA-01400: cannot insert NULL into ("SCHEMA"."USERS"."EMAIL")
 */
public class OracleNotNullStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("ORA-01400") && message.contains("cannot insert NULL");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        // Extract field from: ("SCHEMA"."TABLE"."FIELD")
        String field = extractWithRegex(message, "\\.\"([^\"]+)\"\\)$");

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
