CREATE TABLE financial_goal (
    id BIGINT NOT NULL AUTO_INCREMENT,
    description VARCHAR(255) NOT NULL,
    amount_to_achieve DECIMAL(15, 2) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    initial_date DATE NOT NULL,
    final_date DATE NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_financial_goal PRIMARY KEY (id),
    CONSTRAINT fk_financial_goal_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_financial_goal_amount_to_achieve
        CHECK (amount_to_achieve > 0),
    CONSTRAINT chk_financial_goal_amount
        CHECK (amount > 0),
    CONSTRAINT chk_financial_goal_amount_below_target
        CHECK (amount < amount_to_achieve),
    CONSTRAINT chk_financial_goal_dates
        CHECK (final_date IS NULL OR final_date >= initial_date)
);

CREATE INDEX idx_financial_goal_user_id
    ON financial_goal (user_id);
