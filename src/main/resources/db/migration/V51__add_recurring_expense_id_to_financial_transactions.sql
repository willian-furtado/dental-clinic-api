ALTER TABLE financial_transactions
ADD COLUMN recurring_expense_id VARCHAR(255);

ALTER TABLE financial_transactions
ADD CONSTRAINT fk_financial_transaction_recurring_expense
FOREIGN KEY (recurring_expense_id) REFERENCES recurring_expenses(id);