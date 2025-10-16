package xyz.catuns.spring.base.constraint.strategy.oracle;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * Oracle: ORA-00001: unique constraint (SCHEMA.UK_USERS_EMAIL) violated
 */
public class OracleUniqueStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("ORA-00001") && message.contains("unique constraint");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        // Extract constraint name: SCHEMA.CONSTRAINT_NAME
        String fullConstraintName = extractWithRegex(message, "\\(([^)]+)\\)");
        String constraintName = fullConstraintName != null && fullConstraintName.contains(".")
                ? fullConstraintName.substring(fullConstraintName.lastIndexOf(".") + 1)
                : fullConstraintName;

        // Try to extract field from constraint name (e.g., UK_USERS_EMAIL -> email)
        String field = extractFieldFromConstraintName(constraintName);

        String userMessage = field != null
                ? String.format("A record with this %s already exists", formatFieldName(field))
                : "This record already exists in the database";

        return new ConstraintViolationInfo(
                "UNIQUE_VIOLATION",
                constraintName,
                field,
                null,
                userMessage
        );
    }

    public String extractFieldFromConstraintName(String constraintName) {
        if (constraintName == null) return null;

        // Common patterns: UK_TABLE_FIELD, UQ_TABLE_FIELD, TABLE_FIELD_UK
        String[] patterns = {"UK_[^_]+_(.+)", "UQ_[^_]+_(.+)", "[^_]+_(.+)_UK", "[^_]+_(.+)_UQ"};

        for (String pattern : patterns) {
            String field = extractWithRegex(constraintName, pattern);
            if (field != null) return field;
        }

        return null;
    }
}

