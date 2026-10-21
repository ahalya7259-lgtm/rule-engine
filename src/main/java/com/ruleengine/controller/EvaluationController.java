package com.ruleengine.controller;

import com.ruleengine.dto.EvaluateRequest;
import com.ruleengine.engine.RuleEngine;
import com.ruleengine.model.EvaluationResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Evaluation", description = "Evaluate facts against business rules")
public class EvaluationController {

    private final RuleEngine ruleEngine;

    @PostMapping("/evaluate")
    @Operation(summary = "Evaluate facts and return decision + explanation")
    public EvaluationResult evaluate(@Valid @RequestBody EvaluateRequest request) {
        return ruleEngine.evaluate(request.getFacts());
    }

    /** Convenience endpoint that accepts the facts map directly as body */
    @PostMapping("/evaluate/raw")
    @Operation(summary = "Evaluate using raw facts map as body")
    public EvaluationResult evaluateRaw(@RequestBody Map<String, Object> facts) {
        return ruleEngine.evaluate(facts);
    }
}
