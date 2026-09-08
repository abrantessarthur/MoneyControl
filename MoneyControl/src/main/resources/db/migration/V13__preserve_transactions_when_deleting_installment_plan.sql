ALTER TABLE transactions
    DROP FOREIGN KEY fk_transactions_installment_plan;

ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_installment_plan
        FOREIGN KEY (installment_plan_id)
        REFERENCES installment_plans (id)
        ON DELETE SET NULL;
