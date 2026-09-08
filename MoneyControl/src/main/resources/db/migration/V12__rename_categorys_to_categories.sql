SET @add_category_user = IF(
    EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'categorys' AND column_name = 'user_id'
    ),
    'SELECT 1',
    'ALTER TABLE categorys ADD COLUMN user_id BIGINT NULL'
);
PREPARE add_category_user_statement FROM @add_category_user;
EXECUTE add_category_user_statement;
DEALLOCATE PREPARE add_category_user_statement;

UPDATE categorys
SET user_id = (SELECT MIN(id) FROM users)
WHERE user_id IS NULL;

SET @drop_category_name_unique = IF(
    EXISTS (
        SELECT 1 FROM information_schema.statistics
        WHERE table_schema = DATABASE() AND table_name = 'categorys' AND index_name = 'uk_categorys_name'
    ),
    'ALTER TABLE categorys DROP INDEX uk_categorys_name',
    'SELECT 1'
);
PREPARE drop_category_name_unique_statement FROM @drop_category_name_unique;
EXECUTE drop_category_name_unique_statement;
DEALLOCATE PREPARE drop_category_name_unique_statement;

RENAME TABLE categorys TO categories;

SET @category_user_nullable = (
    SELECT is_nullable FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'categories' AND column_name = 'user_id'
);
SET @make_category_user_required = IF(
    @category_user_nullable = 'YES',
    'ALTER TABLE categories MODIFY COLUMN user_id BIGINT NOT NULL',
    'SELECT 1'
);
PREPARE make_category_user_required_statement FROM @make_category_user_required;
EXECUTE make_category_user_required_statement;
DEALLOCATE PREPARE make_category_user_required_statement;

SET @add_category_user_fk = IF(
    EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_schema = DATABASE() AND table_name = 'categories' AND constraint_name = 'fk_categorys_user'
    ),
    'SELECT 1',
    'ALTER TABLE categories ADD CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE'
);
PREPARE add_category_user_fk_statement FROM @add_category_user_fk;
EXECUTE add_category_user_fk_statement;
DEALLOCATE PREPARE add_category_user_fk_statement;

SET @add_category_user_name_unique = IF(
    EXISTS (
        SELECT 1 FROM information_schema.statistics
        WHERE table_schema = DATABASE() AND table_name = 'categories' AND index_name = 'uk_categorys_user_name'
    ),
    'SELECT 1',
    'ALTER TABLE categories ADD CONSTRAINT uk_categories_user_name UNIQUE (user_id, name)'
);
PREPARE add_category_user_name_unique_statement FROM @add_category_user_name_unique;
EXECUTE add_category_user_name_unique_statement;
DEALLOCATE PREPARE add_category_user_name_unique_statement;

ALTER TABLE financial_goal
    DROP CHECK chk_financial_goal_amount,
    DROP CHECK chk_financial_goal_amount_below_target;

ALTER TABLE financial_goal
    ADD CONSTRAINT chk_financial_goal_amount CHECK (amount >= 0),
    ADD CONSTRAINT chk_financial_goal_amount_below_target CHECK (amount <= amount_to_achieve);
