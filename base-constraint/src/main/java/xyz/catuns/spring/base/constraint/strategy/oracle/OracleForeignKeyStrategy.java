package xyz.catuns.spring.base.constraint.strategy.oracle;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * Oracle: ORA-02291: integrity constraint (SCHEMA.FK_ORDER_USER) violated - parent key not found
 */
public class OracleForeignKeyStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("ORA-02291") ||
                (message.contains("ORA-02292") && message.contains("integrity constraint"));
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String fullConstraintName = extractWithRegex(message, "\\(([^)]+)\\)");
        String constraintName = fullConstraintName != null && fullConstraintName.contains(".")
                ? fullConstraintName.substring(fullConstraintName.lastIndexOf(".") + 1)
                : fullConstraintName;

        // ORA-02291: parent key not found
        // ORA-02292: child record found
        boolean isParentNotFound = message.contains("ORA-02291");

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
