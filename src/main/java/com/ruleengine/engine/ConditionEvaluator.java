package com.ruleengine.engine;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Evaluates simple boolean expressions against a map of facts.
 * Supported: ==, !=, <, >, <=, >=, AND, OR
 * Strings must be quoted with double quotes.
 *
 * Examples:
 *   tenure < 6
 *   contract == "Month-to-month"
 *   tenure < 6 AND monthlyCharges > 80
 */
@Component
public class ConditionEvaluator {

    private static final Pattern COMPARISON = Pattern.compile(
            "\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*(==|!=|<=|>=|<|>)\\s*(\"[^\"]*\"|[0-9]+(?:\\.[0-9]+)?)\\s*"
    );

    public boolean evaluate(String expression, Map<String, Object> facts) {
        if (expression == null || expression.isBlank()) {
            return false;
        }

        String expr = expression.trim();

        // Split by OR first (lowest precedence)
        List<String> orParts = splitByKeyword(expr, "OR");
        for (String orPart : orParts) {
            if (evaluateAnd(orPart.trim(), facts)) {
                return true;
            }
        }
        return false;
    }

    private boolean evaluateAnd(String expression, Map<String, Object> facts) {
        List<String> andParts = splitByKeyword(expression, "AND");
        for (String part : andParts) {
            if (!evaluateComparison(part.trim(), facts)) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateComparison(String expression, Map<String, Object> facts) {
        Matcher m = COMPARISON.matcher(expression);
        if (!m.matches()) {
            throw new IllegalArgumentException("Invalid condition: " + expression);
        }

        String field = m.group(1);
        String operator = m.group(2);
        String rawValue = m.group(3);

        Object actual = facts.get(field);
        if (actual == null) {
            return false; // missing fact → condition fails
        }

        Object expected = parseValue(rawValue);

        return compare(actual, operator, expected);
    }

    private Object parseValue(String raw) {
        if (raw.startsWith("\"") && raw.endsWith("\"")) {
            return raw.substring(1, raw.length() - 1);
        }
        if (raw.contains(".")) {
            return Double.parseDouble(raw);
        }
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException e) {
            return Double.parseDouble(raw);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean compare(Object actual, String operator, Object expected) {
        // Numeric comparison when both are numbers
        if (actual instanceof Number && expected instanceof Number) {
            double a = ((Number) actual).doubleValue();
            double b = ((Number) expected).doubleValue();
            return switch (operator) {
                case "==" -> a == b;
                case "!=" -> a != b;
                case "<"  -> a < b;
                case ">"  -> a > b;
                case "<=" -> a <= b;
                case ">=" -> a >= b;
                default -> false;
            };
        }

        // String / equality comparison
        String a = String.valueOf(actual);
        String b = String.valueOf(expected);
        return switch (operator) {
            case "==" -> a.equals(b);
            case "!=" -> !a.equals(b);
            default -> throw new IllegalArgumentException(
                    "Operator " + operator + " is only supported for numbers");
        };
    }

    /** Split expression by keyword while ignoring it inside quotes (simple version). */
    private List<String> splitByKeyword(String expr, String keyword) {
        List<String> parts = new ArrayList<>();
        String upper = expr.toUpperCase();
        String key = keyword.toUpperCase();
        int start = 0;
        int idx;

        while ((idx = upper.indexOf(key, start)) >= 0) {
            // crude check: not inside quotes
            long quotesBefore = expr.substring(0, idx).chars().filter(c -> c == '"').count();
            if (quotesBefore % 2 == 0) {
                parts.add(expr.substring(start, idx).trim());
                start = idx + key.length();
            } else {
                start = idx + key.length();
            }
        }
        parts.add(expr.substring(start).trim());
        return parts;
    }
}
