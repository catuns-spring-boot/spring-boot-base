package xyz.catuns.spring.base.constraint.strategy.postgresql;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * PostgreSQL: violates foreign key constraint "fk_order_user"
 * Detail: Key (user_id)=(999) is not present in table "users".
 */
public class PostgreSQLForeignKeyStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("violates foreign key constraint");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String constraintName = extractBetween(message, "constraint \"", "\"");
        String field = extractBetween(message, "Key (", ")");
        String value = extractBetween(message, ")=(", ")");
        String referencedTable = extractBetween(message, "table \"", "\"");

        String userMessage = referencedTable != null
                ? String.format("Referenced %s does not exist", formatFieldName(referencedTable))
                : "Referenced record does not exist";

        return new ConstraintViolationInfo(
                "FOREIGN_KEY_VIOLATION",
                constraintName,
                field,
                value,
                userMessage
        );
    }
}
