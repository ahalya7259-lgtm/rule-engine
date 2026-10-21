package com.ruleengine.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class EvaluateRequest {

    /** Facts to evaluate against rules (key-value pairs) */
    @NotNull(message = "facts map is required")
    private Map<String, Object> facts;
}
