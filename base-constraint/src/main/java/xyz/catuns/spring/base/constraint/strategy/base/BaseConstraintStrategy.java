package xyz.catuns.spring.base.constraint.strategy.base;

import xyz.catuns.spring.base.constraint.parser.ConstraintViolationStrategy;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class BaseConstraintStrategy implements ConstraintViolationStrategy {

    protected String extractBetween(String text, String start, String end) {
        if (text == null) return null;

        int startIdx = text.indexOf(start);
        if (startIdx == -1) return null;

        startIdx += start.length();
        int endIdx = text.indexOf(end, startIdx);

        if (endIdx == -1) return null;

        return text.substring(startIdx, endIdx);
    }

    protected String extractWithRegex(String text, String pattern) {
        if (text == null) return null;

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        return m.find() && m.groupCount() > 0 ? m.group(1) : null;
    }

    protected String formatFieldName(String field) {
        if (field == null || field.isEmpty()) return "";

        // Convert snake_case or camelCase to Title Case
        String[] words = field.split("_|(?=[A-Z])");
        return Arrays.stream(words)
                .filter(word -> !word.isEmpty())
                .map(word -> word.substring(0, 1).toUpperCase() +
                        word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    protected String extractFieldFromConstraintName(String constraintName) {
        if (constraintName == null) return null;

        // Common constraint naming patterns
        String[] patterns = {
                "UK_[^_]+_(.+)",     // UK_TABLE_FIELD
                "UQ_[^_]+_(.+)",     // UQ_TABLE_FIELD
                "PK_[^_]+_(.+)",     // PK_TABLE_FIELD
                "FK_[^_]+_(.+)",     // FK_TABLE_FIELD
                "[^_]+_(.+)_UK",     // TABLE_FIELD_UK
                "[^_]+_(.+)_UQ",     // TABLE_FIELD_UQ
                "[^_]+_(.+)_PK",     // TABLE_FIELD_PK
                "[^_]+_(.+)_FK"      // TABLE_FIELD_FK
        };

        for (String pattern : patterns) {
            String field = extractWithRegex(constraintName, pattern);
            if (field != null) return field;
        }

        return null;
    }
}