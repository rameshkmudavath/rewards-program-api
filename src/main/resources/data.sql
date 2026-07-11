-- Safeguard deletion sequences to prevent transactional dependencies breakage
DELETE FROM transaction;
DELETE FROM customer;

-- 1. Populate Master Customer Entries
INSERT INTO customer (id, name, email) VALUES ('CUST01', 'Alice Smith', 'alice@example.com');
INSERT INTO customer (id, name, email) VALUES ('CUST02', 'Bob Jones', 'bob@example.com');

-- 2. Populate Connected Dependent Transactions
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (120.0, CURRENT_DATE(), 'CUST01');
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (80.0, DATEADD('MONTH', -1, CURRENT_DATE()), 'CUST01');
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (150.0, DATEADD('MONTH', -2, CURRENT_DATE()), 'CUST01');
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (40.0, DATEADD('MONTH', -3, CURRENT_DATE()), 'CUST01');

INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (100.0, CURRENT_DATE(), 'CUST02');
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (200.0, DATEADD('MONTH', -1, CURRENT_DATE()), 'CUST02');