package com.ruleengine.engine;

import com.ruleengine.model.EvaluationResult;
import com.ruleengine.model.Rule;
import com.ruleengine.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RuleEngine {

    private final RuleRepository ruleRepository;
    private final ConditionEvaluator conditionEvaluator;
    private final ActionExecutor actionExecutor;

    /**
     * Evaluate all enabled rules (ordered by priority DESC) against the given facts.
     */
    public EvaluationResult evaluate(Map<String, Object> facts) {
        List<Rule> rules = ruleRepository.findByEnabledTrueOrderByPriorityDesc();

        Map<String, Object> decision = new LinkedHashMap<>();
        List<String> firedRules = new ArrayList<>();
        List<String> explanationParts = new ArrayList<>();

        for (Rule rule : rules) {
            try {
                boolean matched = conditionEvaluator.evaluate(rule.getConditionExpression(), facts);
                if (matched) {
                    actionExecutor.execute(rule.getActionExpression(), decision);
                    firedRules.add(rule.getName());
                    explanationParts.add(
                            "Rule '" + rule.getName() + "' matched because " + rule.getConditionExpression()
                    );
                }
            } catch (Exception e) {
                // Skip malformed rules but continue evaluation
                explanationParts.add(
                        "Rule '" + rule.getName() + "' skipped due to error: " + e.getMessage()
                );
            }
        }

        String explanation = explanationParts.isEmpty()
                ? "No rules matched."
                : String.join("; ", explanationParts);

        return EvaluationResult.builder()
                .decision(decision)
                .firedRules(firedRules)
                .explanation(explanation)
                .build();
    }
}
