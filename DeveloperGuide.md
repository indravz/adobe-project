Connecting to Database locally: psql -U postgres (rootpassword)

cd C:\Program Files\PostgreSQL\17\bin
CREATE DATABASE trading_app_db;
\c trading_app_db


CREATE TABLE users (
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
email VARCHAR(100) NOT NULL UNIQUE
);


CREATE TABLE accounts (
id SERIAL PRIMARY KEY,
account_number VARCHAR(20) UNIQUE NOT NULL, -- Unique account identifier
account_type VARCHAR(10) NOT NULL CHECK (account_type IN ('individual', 'joint')), -- 'individual' or 'joint'
user_id BIGINT NOT NULL, -- Foreign key referencing the user owning the account
created_at TIMESTAMP DEFAULT NOW(),
updated_at TIMESTAMP DEFAULT NOW(),
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


ALTER TABLE accounts ADD CONSTRAINT account_type_check CHECK (account_type IN ('individual', 'joint'));



ex:

id	account_number	account_type	user_id	created_at
1	12345	individual	1	2025-01-18 10:00:00
2	67890	joint	2	2025-01-18 10:30:00
3	67890	joint	3	2025-01-18 10:30:00


-- First, create the sequence (outside the table)
CREATE SEQUENCE account_number_seq
START WITH 1000000
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

-- Now create the accounts table
CREATE TABLE accounts (
id SERIAL PRIMARY KEY,
account_number BIGINT UNIQUE NOT NULL DEFAULT nextval('account_number_seq'),
account_type VARCHAR(10) NOT NULL CHECK (account_type IN ('individual', 'joint')),
user_id BIGINT NOT NULL,
created_at TIMESTAMP DEFAULT NOW(),
updated_at TIMESTAMP DEFAULT NOW(),
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


-- Add account_number column if not already there (if using UUID approach, remove UUID column)
ALTER TABLE accounts
ADD COLUMN account_number BIGINT UNIQUE;

-- Set the default value of account_number to be generated from the sequence
ALTER TABLE accounts
ALTER COLUMN account_number SET DEFAULT nextval('account_number_seq');



CREATE TABLE bank_links (
id SERIAL PRIMARY KEY,
account_id BIGINT REFERENCES accounts(id),
bank_name VARCHAR(100),
bank_account_number VARCHAR(100),
available_balance DECIMAL(15, 2)
);

CREATE TABLE trades (
id SERIAL PRIMARY KEY,
account_id BIGINT NOT NULL,
symbol VARCHAR(10) NOT NULL,
action VARCHAR(10) NOT NULL CHECK (action IN ('BUY', 'SELL')),  -- Ensure valid values for action
amount NUMERIC(15, 2) NOT NULL,
trade_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE  -- Ensure referential integrity
);

-- Optional: Create index for faster queries based on account_id or symbol
CREATE INDEX idx_account_id ON trades(account_id);
CREATE INDEX idx_symbol ON trades(symbol);



ALTER TABLE bank_links
ADD COLUMN available_cash NUMERIC(15, 2);
