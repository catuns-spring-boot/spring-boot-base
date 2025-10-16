package xyz.catuns.spring.base.constraint.strategy.generic;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.strategy.base.BaseConstraintStrategy;

public class GenericStrategy extends BaseConstraintStrategy {

    @Override
    public boolean matches(String message) {
        return true; // Always matches as fallback
    }

    @Override
    public ConstraintViolationInfo parse(String message) {
        // Try to detect type from keywords
        String type = detectViolationType(message);
        String genericMessage = generateGenericMessage(type, message);

        return new ConstraintViolationInfo(
                type,
                null,
                null,
                null,
                genericMessage
        );
    }

    private String detectViolationType(String message) {
        String lowerMessage = message.toLowerCase();

        if (lowerMessage.contains("unique") || lowerMessage.contains("duplicate")) {
            return "UNIQUE_VIOLATION";
        }
        if (lowerMessage.contains("foreign key") || lowerMessage.contains("reference")) {
            return "FOREIGN_KEY_VIOLATION";
        }
        if (lowerMessage.contains("not null") || lowerMessage.contains("null")) {
            return "NOT_NULL_VIOLATION";
        }
        if (lowerMessage.contains("check constraint")) {
            return "CHECK_VIOLATION";
        }

        return "DATA_INTEGRITY_VIOLATION";
    }

    private String generateGenericMessage(String type, String originalMessage) {
        return switch (type) {
            case "UNIQUE_VIOLATION" -> "This record already exists in the database";
            case "FOREIGN_KEY_VIOLATION" -> "Referenced record does not exist or is being used by other records";
            case "NOT_NULL_VIOLATION" -> "A required field is missing";
            case "CHECK_VIOLATION" -> "Data validation failed";
            default -> "Data integrity constraint violated. Please check your input data.";
        };
    }
}