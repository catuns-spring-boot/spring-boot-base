package xyz.catuns.spring.base.constraint.parser;

import java.util.List;

public class ConstraintViolationParser {

    private final List<ConstraintViolationStrategy> strategies;

    public ConstraintViolationParser(List<ConstraintViolationStrategy> strategies) {
        this.strategies = strategies;
    }

    public ConstraintViolationInfo parse(String message) {
        if (message == null || message.isBlank()) {
            return createDefault();
        }

        return strategies.stream()
                .filter(strategy -> strategy.matches(message))
                .findFirst()
                .map(strategy -> strategy.parse(message))
                .orElse(createDefault());
    }

    private ConstraintViolationInfo createDefault() {
        return new ConstraintViolationInfo(
                "DATA_INTEGRITY_VIOLATION",
                null,
                null,
                null,
                "Data integrity constraint violated. Please check your input data."
        );
    }
}