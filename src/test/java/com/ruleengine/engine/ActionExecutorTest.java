package com.ruleengine.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ActionExecutorTest {

    private ActionExecutor executor;

    @BeforeEach
    void setUp() {
        executor = new ActionExecutor();
    }

    @Test
    void stringAssignment() {
        Map<String, Object> decision = new HashMap<>();
        executor.execute("risk = \"HIGH\"", decision);
        assertEquals("HIGH", decision.get("risk"));
    }

    @Test
    void numericAssignment() {
        Map<String, Object> decision = new HashMap<>();
        executor.execute("score = 90", decision);
        assertEquals(90L, decision.get("score"));
    }

    @Test
    void multipleActions() {
        Map<String, Object> decision = new HashMap<>();
        executor.execute("risk = \"HIGH\"; score = 95", decision);
        assertEquals("HIGH", decision.get("risk"));
        assertEquals(95L, decision.get("score"));
    }
}
