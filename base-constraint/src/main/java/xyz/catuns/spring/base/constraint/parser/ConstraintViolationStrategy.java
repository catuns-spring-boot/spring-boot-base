package xyz.catuns.spring.base.constraint.parser;

public interface ConstraintViolationStrategy {
    boolean matches(String message);
    ConstraintViolationInfo parse(String message);
}