ALTER TABLE financial_goal
    DROP CHECK chk_financial_goal_amount,
    DROP CHECK chk_financial_goal_amount_below_target;

ALTER TABLE financial_goal
    ADD CONSTRAINT chk_financial_goal_amount
        CHECK (amount >= 0),
    ADD CONSTRAINT chk_financial_goal_amount_below_target
        CHECK (amount <= amount_to_achieve);
