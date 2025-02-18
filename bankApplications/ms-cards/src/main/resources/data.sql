CREATE TABLE IF NOT EXISTS cards (
                                     card_id INT AUTO_INCREMENT PRIMARY KEY,
                                     customer_id INT NOT NULL,
                                     card_number VARCHAR(255) NOT NULL,
    card_type VARCHAR(50) NOT NULL,
    total_limit INT NOT NULL,
    amount_used INT NOT NULL,
    available_amount INT NOT NULL,
    create_dt DATE NOT NULL
    );

INSERT INTO cards (customer_id, card_number, card_type, total_limit, amount_used, available_amount, create_dt)
VALUES
    (1, '1234-5678-9012-3456', 'VISA', 5000, 1500, 3500, '2024-01-01'),
    (2, '9876-5432-1098-7654', 'MASTERCARD', 7000, 2000, 5000, '2024-02-01'),
    (1, '1122-3344-5566-7788', 'AMEX', 10000, 3000, 7000, '2024-03-01');

CREATE SEQUENCE customer_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE accounts
(
    account_number BIGINT NOT NULL,
    customer_id    INT,
    account_type   VARCHAR(255),
    branch_address VARCHAR(255),
    create_dt      date,
    CONSTRAINT pk_accounts PRIMARY KEY (account_number)
);

CREATE TABLE customer
(
    customer_id   INT NOT NULL,
    name          VARCHAR(255),
    email         VARCHAR(255),
    mobile_number VARCHAR(255),
    create_dt     date,
    CONSTRAINT pk_customer PRIMARY KEY (customer_id)
);