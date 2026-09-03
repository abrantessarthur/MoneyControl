SET @drop_old_unique = IF(
    EXISTS (
        SELECT 1
        FROM information_schema.statistics
        WHERE table_schema = DATABASE()
          AND table_name = 'categorys'
          AND index_name = 'uk_categorys_name'
    ),
    'ALTER TABLE categorys DROP INDEX uk_categorys_name',
    'SELECT 1'
);
PREPARE drop_old_unique_statement FROM @drop_old_unique;
EXECUTE drop_old_unique_statement;
DEALLOCATE PREPARE drop_old_unique_statement;

SET @add_user_id = IF(
    EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = DATABASE()
          AND table_name = 'categorys'
          AND column_name = 'user_id'
    ),
    'SELECT 1',
    'ALTER TABLE categorys ADD COLUMN user_id BIGINT NULL'
);
PREPARE add_user_id_statement FROM @add_user_id;
EXECUTE add_user_id_statement;
DEALLOCATE PREPARE add_user_id_statement;

DROP TABLE IF EXISTS migration_v10_category_user_mapping;
DROP TABLE IF EXISTS migration_v10_category_user_pairs;

CREATE TABLE migration_v10_category_user_pairs (
    category_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (category_id, user_id)
);

INSERT IGNORE INTO migration_v10_category_user_pairs (category_id, user_id)
SELECT category_id, user_id FROM transactions
UNION
SELECT category_id, user_id FROM monthly_budgets
UNION
SELECT category_id, user_id FROM installment_plans
UNION
SELECT category_id, user_id FROM recurring_transactions;

INSERT IGNORE INTO migration_v10_category_user_pairs (category_id, user_id)
SELECT c.id, (SELECT MIN(u.id) FROM users u)
FROM categorys c
WHERE NOT EXISTS (
    SELECT 1
    FROM migration_v10_category_user_pairs cup
    WHERE cup.category_id = c.id
)
AND EXISTS (SELECT 1 FROM users);

UPDATE categorys c
JOIN (
    SELECT category_id, MIN(user_id) AS user_id
    FROM migration_v10_category_user_pairs
    GROUP BY category_id
) owner ON owner.category_id = c.id
SET c.user_id = owner.user_id;

INSERT INTO categorys (name, user_id)
SELECT original.name, cup.user_id
FROM migration_v10_category_user_pairs cup
JOIN categorys original ON original.id = cup.category_id
WHERE cup.user_id <> original.user_id;

CREATE TABLE migration_v10_category_user_mapping (
    old_category_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    new_category_id BIGINT NOT NULL,
    PRIMARY KEY (old_category_id, user_id)
);

INSERT INTO migration_v10_category_user_mapping (old_category_id, user_id, new_category_id)
SELECT cup.category_id, cup.user_id, owned.id
FROM migration_v10_category_user_pairs cup
JOIN categorys original ON original.id = cup.category_id
JOIN categorys owned
    ON owned.name = original.name
    AND owned.user_id = cup.user_id;

UPDATE transactions t
JOIN migration_v10_category_user_mapping m
    ON m.old_category_id = t.category_id
    AND m.user_id = t.user_id
SET t.category_id = m.new_category_id;

UPDATE monthly_budgets b
JOIN migration_v10_category_user_mapping m
    ON m.old_category_id = b.category_id
    AND m.user_id = b.user_id
SET b.category_id = m.new_category_id;

UPDATE installment_plans i
JOIN migration_v10_category_user_mapping m
    ON m.old_category_id = i.category_id
    AND m.user_id = i.user_id
SET i.category_id = m.new_category_id;

UPDATE recurring_transactions r
JOIN migration_v10_category_user_mapping m
    ON m.old_category_id = r.category_id
    AND m.user_id = r.user_id
SET r.category_id = m.new_category_id;

ALTER TABLE categorys
    MODIFY COLUMN user_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_categorys_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    ADD CONSTRAINT uk_categorys_user_name UNIQUE (user_id, name);

CREATE INDEX idx_categorys_user_id ON categorys (user_id);

DROP TABLE migration_v10_category_user_mapping;
DROP TABLE migration_v10_category_user_pairs;
