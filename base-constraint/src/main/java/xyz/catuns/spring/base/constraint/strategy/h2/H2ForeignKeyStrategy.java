package xyz.catuns.spring.base.constraint.strategy.h2;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * H2: Referential integrity constraint violation: "FK_ORDER_USER: PUBLIC.ORDERS FOREIGN KEY(USER_ID) REFERENCES PUBLIC.USERS(ID) (1)"
 */
public class H2ForeignKeyStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("Referential integrity constraint violation");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String constraintName = extractBetween(message, "\"", ":");
        String referencedTable = extractWithRegex(message, "REFERENCES\\s+(\\w+\\.\\w+)");

        String userMessage = referencedTable != null
                ? String.format("Referenced %s does not exist", formatFieldName(referencedTable))
                : "Referenced record does not exist";

        return new ConstraintViolationInfo(
                "FOREIGN_KEY_VIOLATION",
                constraintName,
                null,
                null,
                userMessage
        );
    }
}
