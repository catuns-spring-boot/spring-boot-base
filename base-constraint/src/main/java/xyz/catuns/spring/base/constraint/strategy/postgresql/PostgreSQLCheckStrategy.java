package xyz.catuns.spring.base.constraint.strategy.postgresql;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

// PostgreSQL: new row for relation "users" violates check constraint "check_age"
public class PostgreSQLCheckStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("violates check constraint");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String constraintName = extractBetween(message, "constraint \"", "\"");

        return new ConstraintViolationInfo(
                "CHECK_VIOLATION",
                constraintName,
                null,
                null,
                "Data validation failed: " + (constraintName != null ? formatFieldName(constraintName) : "check constraint violated")
        );
    }
}
