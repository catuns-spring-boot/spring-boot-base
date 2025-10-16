package xyz.catuns.spring.base.constraint.strategy.oracle;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

/**
 * Oracle: ORA-02290: check constraint (SCHEMA.CHK_AGE) violated
 */
public class OracleCheckStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return message.contains("ORA-02290") && message.contains("check constraint");
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        String fullConstraintName = extractWithRegex(message, "\\(([^)]+)\\)");
        String constraintName = fullConstraintName != null && fullConstraintName.contains(".")
                ? fullConstraintName.substring(fullConstraintName.lastIndexOf(".") + 1)
                : fullConstraintName;

        return new ConstraintViolationInfo(
                "CHECK_VIOLATION",
                constraintName,
                null,
                null,
                "Data validation failed: " + (constraintName != null ? formatFieldName(constraintName) : "check constraint violated")
        );
    }
}
