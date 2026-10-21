package com.ruleengine.engine;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Executes simple assignment actions and writes results into the decision map.
 * Supported form:  key = value
 * Examples:
 *   risk = "HIGH"
 *   score = 90
 *   paymentRisk = "ELEVATED"
 */
@Component
public class ActionExecutor {

    private static final Pattern ASSIGNMENT = Pattern.compile(
            "\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*(\"[^\"]*\"|[0-9]+(?:\\.[0-9]+)?)\\s*"
    );

    public void execute(String actionExpression, Map<String, Object> decision) {
        if (actionExpression == null || actionExpression.isBlank()) {
            return;
        }

        // Support multiple actions separated by semicolon
        String[] parts = actionExpression.split(";");
        for (String part : parts) {
            executeSingle(part.trim(), decision);
        }
    }

    private void executeSingle(String expression, Map<String, Object> decision) {
        Matcher m = ASSIGNMENT.matcher(expression);
        if (!m.matches()) {
            throw new IllegalArgumentException("Invalid action: " + expression);
        }

        String key = m.group(1);
        String rawValue = m.group(2);
        Object value = parseValue(rawValue);
        decision.put(key, value);
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
}
