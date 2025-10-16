package xyz.catuns.spring.base.constraint.strategy.sqlserver;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * SQL Server: Cannot insert the value NULL into column 'Email', table 'dbo.Users'
 */
public class SQLServerNotNullStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("Cannot insert the value NULL into column");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String field = extractBetween(message, "column '", "'");

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
