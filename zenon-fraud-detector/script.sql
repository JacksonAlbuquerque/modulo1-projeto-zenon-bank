USE zenon;

CREATE TABLE TRANSACTIONS
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    step             INT            NOT NULL,
    type             ENUM('CASH_IN', 'CASH_OUT', 'DEBIT', 'PAYMENT', 'TRANSFER') NOT NULL,
    amount           DECIMAL(15, 2) NOT NULL,
    name_orig        VARCHAR(20)    NOT NULL,
    old_balance_orig DECIMAL(15, 2) NOT NULL,
    new_balance_orig DECIMAL(15, 2) NOT NULL,
    name_dest        VARCHAR(20)    NOT NULL,
    old_balance_dest DECIMAL(15, 2) NOT NULL,
    new_balance_dest DECIMAL(15, 2) NOT NULL,
    is_fraud         BOOLEAN        NOT NULL,
    is_flagged_fraud BOOLEAN        NOT NULL,
    INDEX            idx_name_orig (name_orig)
);