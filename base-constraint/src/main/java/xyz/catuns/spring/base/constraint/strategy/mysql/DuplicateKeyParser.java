package xyz.catuns.spring.base.constraint.strategy.mysql;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;

interface DuplicateKeyParser {

    String formatFieldName(String field);

    default ConstraintViolationInfo getConstraintViolationInfo(String key, String value) {
        String field = key != null && key.contains(".")
                ? key.substring(key.lastIndexOf(".") + 1)
                : key;

        String userMessage = field != null
                ? String.format("A record with this %s already exists", formatFieldName(field))
                : "This record already exists";

        return new ConstraintViolationInfo(
                "UNIQUE_VIOLATION",
                key,
                field,
                value,
                userMessage
        );
    }
}
