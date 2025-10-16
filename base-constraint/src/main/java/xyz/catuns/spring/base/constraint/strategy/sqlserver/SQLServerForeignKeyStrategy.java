package xyz.catuns.spring.base.constraint.strategy.sqlserver;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * SQL Server: The INSERT statement conflicted with the FOREIGN KEY constraint "FK_Order_User".
 * The conflict occurred in database "MyDB", table "dbo.Users", column 'id'.
 */
public class SQLServerForeignKeyStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("conflicted with the FOREIGN KEY constraint") ||
                message.contains("conflicted with the REFERENCE constraint");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String constraintName = extractBetween(message, "constraint \"", "\"");
        String referencedTable = extractBetween(message, "table \"", "\"");
        String referencedColumn = extractBetween(message, "column '", "'");

        String userMessage = referencedTable != null
                ? String.format("Referenced %s does not exist", formatFieldName(referencedTable))
                : "Referenced record does not exist";

        return new ConstraintViolationInfo(
                "FOREIGN_KEY_VIOLATION",
                constraintName,
                referencedColumn,
                null,
                userMessage
        );
    }
}
