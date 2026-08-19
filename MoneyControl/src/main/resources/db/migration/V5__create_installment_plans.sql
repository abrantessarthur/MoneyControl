CREATE TABLE installment_plans (
    id BIGINT NOT NULL AUTO_INCREMENT,
    description VARCHAR(255) NOT NULL,
    total_amount DECIMAL(15, 2) NOT NULL,
    total_installments INT NOT NULL,
    first_due_date DATE NOT NULL,
    category_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_installment_plans PRIMARY KEY (id),
    CONSTRAINT fk_installment_plans_category
        FOREIGN KEY (category_id) REFERENCES categorys (id),
    CONSTRAINT fk_installment_plans_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

ALTER TABLE transactions
    ADD COLUMN installment_plan_id BIGINT NULL,
    ADD COLUMN installment_number INT NULL,
    ADD CONSTRAINT fk_transactions_installment_plan
        FOREIGN KEY (installment_plan_id) REFERENCES installment_plans (id);
