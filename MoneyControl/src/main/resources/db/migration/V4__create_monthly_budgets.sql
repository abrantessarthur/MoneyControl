CREATE TABLE monthly_budgets (
    id BIGINT NOT NULL AUTO_INCREMENT,
    limit_amount DECIMAL(15, 2) NOT NULL,
    month CHAR(7) NOT NULL,
    category_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_monthly_budgets PRIMARY KEY (id),
    CONSTRAINT uk_monthly_budgets_user_category_month
        UNIQUE (user_id, category_id, month),
    CONSTRAINT fk_monthly_budgets_category
        FOREIGN KEY (category_id) REFERENCES categorys (id),
    CONSTRAINT fk_monthly_budgets_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
