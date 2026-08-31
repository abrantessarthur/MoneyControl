CREATE TABLE recurring_transactions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    type_transactional VARCHAR(255) NOT NULL,
    frequency VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    next_execution_date DATE NOT NULL,
    end_date DATE NULL,
    last_execution_date DATE NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    category_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_recurring_transactions PRIMARY KEY (id),
    CONSTRAINT fk_recurring_transactions_category
        FOREIGN KEY (category_id) REFERENCES categorys (id),
    CONSTRAINT fk_recurring_transactions_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_recurring_transactions_amount
        CHECK (amount > 0)
);

CREATE INDEX idx_recurring_transactions_user_id
    ON recurring_transactions (user_id);

CREATE INDEX idx_recurring_transactions_due
    ON recurring_transactions (active, next_execution_date);

ALTER TABLE transactions
    ADD COLUMN recurring_transaction_id BIGINT NULL,
    ADD COLUMN recurrence_reference_date DATE NULL,
    ADD CONSTRAINT fk_transactions_recurring_transaction
        FOREIGN KEY (recurring_transaction_id)
        REFERENCES recurring_transactions (id)
        ON DELETE SET NULL,
    ADD CONSTRAINT uk_transactions_recurring_reference
        UNIQUE (recurring_transaction_id, recurrence_reference_date);
