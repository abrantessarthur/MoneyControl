CREATE TABLE credit_cards (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    last_four_digits CHAR(4) NOT NULL,
    credit_limit DECIMAL(15, 2) NOT NULL,
    closing_day INT NOT NULL,
    due_day INT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_credit_cards PRIMARY KEY (id),
    CONSTRAINT fk_credit_cards_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_credit_cards_credit_limit
        CHECK (credit_limit > 0),
    CONSTRAINT chk_credit_cards_closing_day
        CHECK (closing_day BETWEEN 1 AND 31),
    CONSTRAINT chk_credit_cards_due_day
        CHECK (due_day BETWEEN 1 AND 31)
);

CREATE INDEX idx_credit_cards_user_id ON credit_cards (user_id);
