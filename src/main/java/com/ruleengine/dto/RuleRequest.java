package com.ruleengine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RuleRequest {

    @NotBlank(message = "name is required")
    private String name;

    private String description;

    @NotNull(message = "priority is required")
    private Integer priority;

    @NotBlank(message = "conditionExpression is required")
    private String conditionExpression;

    @NotBlank(message = "actionExpression is required")
    private String actionExpression;

    private Boolean enabled = true;
}
