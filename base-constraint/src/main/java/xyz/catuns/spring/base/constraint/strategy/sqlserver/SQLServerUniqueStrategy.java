package xyz.catuns.spring.base.constraint.strategy.sqlserver;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * SQL Server: Violation of UNIQUE KEY constraint 'UQ_Users_Email'.
 * Cannot insert duplicate key in object 'dbo.Users'. The duplicate key value is (test@example.com).
 */
public class SQLServerUniqueStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("Violation of UNIQUE KEY constraint") ||
                message.contains("Violation of PRIMARY KEY constraint");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String constraintName = extractBetween(message, "constraint '", "'");
        String value = extractBetween(message, "key value is (", ")");

        String field = extractFieldFromConstraintName(constraintName);

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

    public String extractFieldFromConstraintName(String constraintName) {
        if (constraintName == null) return null;
        String[] patterns = {"UQ_[^_]+_(.+)", "PK_[^_]+_(.+)", "[^_]+_(.+)_UQ"};

        for (String pattern : patterns) {
            String field = extractWithRegex(constraintName, pattern);
            if (field != null) return field;
        }
        return null;
    }
}


