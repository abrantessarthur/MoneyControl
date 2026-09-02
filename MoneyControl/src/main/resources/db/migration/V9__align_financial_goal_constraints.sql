ALTER TABLE financial_goal
    DROP CHECK chk_financial_goal_amount,
    DROP CHECK chk_financial_goal_amount_below_target,
    ADD CONSTRAINT chk_financial_goal_amount
        CHECK (amount >= 0),
    ADD CONSTRAINT chk_financial_goal_amount_not_above_target
        CHECK (amount <= amount_to_achieve),
    MODIFY COLUMN final_date DATE NOT NULL;
