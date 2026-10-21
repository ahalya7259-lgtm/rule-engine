package com.ruleengine.controller;

import com.ruleengine.dto.RuleRequest;
import com.ruleengine.dto.RuleResponse;
import com.ruleengine.service.RuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
@Tag(name = "Rules", description = "CRUD operations for business rules")
public class RuleController {

    private final RuleService ruleService;

    @GetMapping
    @Operation(summary = "List all rules")
    public List<RuleResponse> list() {
        return ruleService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get rule by ID")
    public RuleResponse get(@PathVariable Long id) {
        return ruleService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new rule")
    public RuleResponse create(@Valid @RequestBody RuleRequest request) {
        return ruleService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing rule")
    public RuleResponse update(@PathVariable Long id, @Valid @RequestBody RuleRequest request) {
        return ruleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a rule")
    public void delete(@PathVariable Long id) {
        ruleService.delete(id);
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Enable or disable a rule")
    public RuleResponse toggle(@PathVariable Long id) {
        return ruleService.toggle(id);
    }
}
