package com.ruleengine.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConditionEvaluatorTest {

    private ConditionEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new ConditionEvaluator();
    }

    @Test
    void numericLessThan() {
        assertTrue(evaluator.evaluate("tenure < 6", Map.of("tenure", 4)));
        assertFalse(evaluator.evaluate("tenure < 6", Map.of("tenure", 10)));
    }

    @Test
    void numericGreaterThan() {
        assertTrue(evaluator.evaluate("monthlyCharges > 80", Map.of("monthlyCharges", 85)));
        assertFalse(evaluator.evaluate("monthlyCharges > 80", Map.of("monthlyCharges", 50)));
    }

    @Test
    void stringEquals() {
        assertTrue(evaluator.evaluate("contract == \"Month-to-month\"",
                Map.of("contract", "Month-to-month")));
        assertFalse(evaluator.evaluate("contract == \"Month-to-month\"",
                Map.of("contract", "One year")));
    }

    @Test
    void andExpression() {
        Map<String, Object> facts = Map.of(
                "tenure", 4,
                "monthlyCharges", 85,
                "contract", "Month-to-month"
        );
        assertTrue(evaluator.evaluate(
                "tenure < 6 AND monthlyCharges > 80 AND contract == \"Month-to-month\"", facts));
    }

    @Test
    void orExpression() {
        Map<String, Object> facts = Map.of("tenure", 30, "monthlyCharges", 40);
        assertTrue(evaluator.evaluate("tenure >= 24 OR monthlyCharges > 100", facts));
        assertFalse(evaluator.evaluate("tenure < 5 OR monthlyCharges > 100", facts));
    }

    @Test
    void missingFactReturnsFalse() {
        assertFalse(evaluator.evaluate("tenure < 6", Map.of()));
    }

    @Test
    void notEquals() {
        assertTrue(evaluator.evaluate("contract != \"Two year\"",
                Map.of("contract", "Month-to-month")));
    }
}
