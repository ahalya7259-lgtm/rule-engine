package com.ruleengine.service;

import com.ruleengine.dto.RuleRequest;
import com.ruleengine.dto.RuleResponse;
import com.ruleengine.model.Rule;
import com.ruleengine.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RuleService {

    private final RuleRepository ruleRepository;

    public List<RuleResponse> findAll() {
        return ruleRepository.findAll().stream()
                .map(RuleResponse::from)
                .toList();
    }

    public RuleResponse findById(Long id) {
        return RuleResponse.from(getRule(id));
    }

    @Transactional
    public RuleResponse create(RuleRequest request) {
        if (ruleRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Rule name already exists");
        }
        Rule rule = mapToEntity(request, new Rule());
        return RuleResponse.from(ruleRepository.save(rule));
    }

    @Transactional
    public RuleResponse update(Long id, RuleRequest request) {
        Rule rule = getRule(id);
        if (ruleRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Rule name already exists");
        }
        mapToEntity(request, rule);
        return RuleResponse.from(ruleRepository.save(rule));
    }

    @Transactional
    public void delete(Long id) {
        if (!ruleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rule not found");
        }
        ruleRepository.deleteById(id);
    }

    @Transactional
    public RuleResponse toggle(Long id) {
        Rule rule = getRule(id);
        rule.setEnabled(!Boolean.TRUE.equals(rule.getEnabled()));
        return RuleResponse.from(ruleRepository.save(rule));
    }

    private Rule getRule(Long id) {
        return ruleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rule not found"));
    }

    private Rule mapToEntity(RuleRequest req, Rule rule) {
        rule.setName(req.getName());
        rule.setDescription(req.getDescription());
        rule.setPriority(req.getPriority());
        rule.setConditionExpression(req.getConditionExpression());
        rule.setActionExpression(req.getActionExpression());
        rule.setEnabled(req.getEnabled() != null ? req.getEnabled() : true);
        return rule;
    }
}
