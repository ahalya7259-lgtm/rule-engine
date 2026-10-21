-- Sample churn-related business rules

INSERT INTO rules (name, description, priority, condition_expression, action_expression, enabled, created_at, updated_at)
VALUES
('High Churn Risk',
 'Customers with short tenure, high charges and month-to-month contract',
 100,
 'tenure < 6 AND monthlyCharges > 80 AND contract == "Month-to-month"',
 'risk = "HIGH"',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Medium Churn Risk',
 'Medium tenure or moderately high charges',
 50,
 'tenure < 12 AND monthlyCharges > 60',
 'risk = "MEDIUM"',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Low Churn Risk - Long Tenure',
 'Loyal customers with long tenure',
 20,
 'tenure >= 24',
 'risk = "LOW"',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Fiber Optic High Risk',
 'Fiber optic users on month-to-month often churn',
 80,
 'internetService == "Fiber optic" AND contract == "Month-to-month"',
 'risk = "HIGH"',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Electronic Check Warning',
 'Electronic check payment method is associated with higher churn',
 40,
 'paymentMethod == "Electronic check"',
 'paymentRisk = "ELEVATED"',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
