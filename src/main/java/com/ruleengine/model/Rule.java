package com.ruleengine.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    /** Higher value = higher priority (executed first) */
    @Column(nullable = false)
    private Integer priority = 0;

    /** e.g. tenure < 6 AND monthlyCharges > 80 */
    @Column(nullable = false, length = 1000, name = "condition_expression")
    private String conditionExpression;

    /** e.g. risk = "HIGH" */
    @Column(nullable = false, length = 500, name = "action_expression")
    private String actionExpression;

    @Column(nullable = false)
    private Boolean enabled = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
