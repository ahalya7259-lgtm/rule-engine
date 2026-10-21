# Rule Engine for Business Decisions

A configurable **Business Rule Engine** built with Java 21 and Spring Boot 3.

Rules are stored in the database and evaluated at runtime against incoming facts.  
Change business logic **without redeploying** the application.

---

## Features

- CRUD for rules (create, read, update, delete, enable/disable)
- Custom expression evaluator (`==`, `!=`, `<`, `>`, `<=`, `>=`, `AND`, `OR`)
- Priority-based execution (higher priority rules run first)
- Decision + explanation of which rules fired
- Sample churn-risk rules included
- REST API + Swagger UI
- Unit tests for the engine

---

## Quick Start

```bash
git clone https://github.com/ahalya7259-lgtm/rule-engine.git
cd rule-engine
mvn spring-boot:run
```

- API Base: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console  
  (JDBC URL: `jdbc:h2:mem:ruledb`)

---

## Example

### Evaluate rules

```bash
curl -X POST http://localhost:8080/api/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "tenure": 4,
    "monthlyCharges": 85,
    "contract": "Month-to-month",
    "internetService": "Fiber optic",
    "paymentMethod": "Electronic check"
  }'
```

**Sample response:**

```json
{
  "decision": {
    "risk": "HIGH",
    "paymentRisk": "ELEVATED"
  },
  "firedRules": [
    "High Churn Risk",
    "Fiber Optic High Risk",
    "Electronic Check Warning"
  ],
  "explanation": "Rule 'High Churn Risk' matched because tenure < 6 AND monthlyCharges > 80 AND contract == \"Month-to-month\"; Rule 'Fiber Optic High Risk' matched because internetService == \"Fiber optic\" AND contract == \"Month-to-month\"; Rule 'Electronic Check Warning' matched because paymentMethod == \"Electronic check\""
}
```

### Manage rules

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/rules` | List all rules |
| GET | `/api/rules/{id}` | Get one rule |
| POST | `/api/rules` | Create rule |
| PUT | `/api/rules/{id}` | Update rule |
| DELETE | `/api/rules/{id}` | Delete rule |
| PATCH | `/api/rules/{id}/toggle` | Enable / disable |

---

## Architecture

```
Client
  │
  ▼
Controller  →  Service  →  RuleEngine
                              │
                    ┌─────────┴─────────┐
                    ▼                   ▼
            ConditionEvaluator    ActionExecutor
                    │
                    ▼
              Rule Repository (DB)
```

1. Client sends facts (JSON map)
2. Engine loads enabled rules ordered by priority (DESC)
3. ConditionEvaluator checks each rule against facts
4. ActionExecutor applies actions of matched rules
5. Result contains final decision map + fired rule names + human explanation

---

## Project Structure

```
com.ruleengine
├── model          # Rule entity, EvaluationResult
├── repository     # Spring Data JPA
├── engine         # RuleEngine, ConditionEvaluator, ActionExecutor
├── service        # Business logic
├── controller     # REST endpoints
├── dto            # Request/Response objects
└── RuleEngineApplication.java
```

---

## Sample Rules (loaded on startup)

| Rule | Priority | Condition |
|------|----------|-----------|
| High Churn Risk | 100 | tenure < 6 AND monthlyCharges > 80 AND contract == "Month-to-month" |
| Fiber Optic High Risk | 80 | internetService == "Fiber optic" AND contract == "Month-to-month" |
| Medium Churn Risk | 50 | tenure < 12 AND monthlyCharges > 60 |
| Electronic Check Warning | 40 | paymentMethod == "Electronic check" |
| Low Churn Risk - Long Tenure | 20 | tenure >= 24 |

---

## Author

**Ahalya** — BCA Graduate | Aspiring Data Analyst  
GitHub: [ahalya7259-lgtm](https://github.com/ahalya7259-lgtm)

## License

MIT
