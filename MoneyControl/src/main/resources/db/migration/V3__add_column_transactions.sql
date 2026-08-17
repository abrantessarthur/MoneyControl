ALTER TABLE transactions
    ADD COLUMN user_id BIGINT NULL;

UPDATE transactions
SET user_id = (SELECT MIN(id) FROM users)
WHERE user_id IS NULL;

ALTER TABLE transactions
    MODIFY COLUMN user_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_transactions_user
        FOREIGN KEY (user_id) REFERENCES users (id);
