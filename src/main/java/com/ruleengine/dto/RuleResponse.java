package com.ruleengine.dto;

import com.ruleengine.model.Rule;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class RuleResponse {

    private Long id;
    private String name;
    private String description;
    private Integer priority;
    private String conditionExpression;
    private String actionExpression;
    private Boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;

    public static RuleResponse from(Rule rule) {
        return RuleResponse.builder()
                .id(rule.getId())
                .name(rule.getName())
                .description(rule.getDescription())
                .priority(rule.getPriority())
                .conditionExpression(rule.getConditionExpression())
                .actionExpression(rule.getActionExpression())
                .enabled(rule.getEnabled())
                .createdAt(rule.getCreatedAt())
                .updatedAt(rule.getUpdatedAt())
                .build();
    }
}
