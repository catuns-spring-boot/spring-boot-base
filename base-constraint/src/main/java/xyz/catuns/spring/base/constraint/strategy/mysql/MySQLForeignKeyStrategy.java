package xyz.catuns.spring.base.constraint.strategy.mysql;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

public class MySQLForeignKeyStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("Cannot add or update a child row") ||
                message.contains("Cannot delete or update a parent row");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String constraintName = extractBetween(message, "CONSTRAINT `", "`");

        boolean isParentNotFound = message.contains("Cannot add or update a child row");
        String userMessage = isParentNotFound
                ? "Referenced record does not exist"
                : "Cannot delete record because it is referenced by other records";

        return new ConstraintViolationInfo(
                "FOREIGN_KEY_VIOLATION",
                constraintName,
                null,
                null,
                userMessage
        );
    }
}
