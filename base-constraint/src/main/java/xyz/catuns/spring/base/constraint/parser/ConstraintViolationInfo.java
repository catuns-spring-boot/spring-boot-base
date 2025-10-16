package xyz.catuns.spring.base.constraint.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConstraintViolationInfo {
    private String type;  // UNIQUE, FOREIGN_KEY, NOT_NULL, CHECK
    private String constraintName;
    private String field;
    private String value;
    private String userFriendlyMessage;
}