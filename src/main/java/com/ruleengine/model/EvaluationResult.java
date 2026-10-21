package com.ruleengine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationResult {

    /** Final decision key-value pairs produced by actions */
    @Builder.Default
    private Map<String, Object> decision = new LinkedHashMap<>();

    /** Names of rules that matched */
    @Builder.Default
    private List<String> firedRules = new ArrayList<>();

    /** Human-readable explanation of why rules fired */
    private String explanation;
}
