-- Create wallets table
IF OBJECT_ID('wallets', 'U') IS NULL
BEGIN
    CREATE TABLE wallets (
        id BIGINT IDENTITY(1,1) NOT NULL,
        first_name VARCHAR(50) NOT NULL,
        last_name VARCHAR(50) NOT NULL,
        merchant_id VARCHAR(30) NOT NULL,
        wallet_id VARCHAR(10) NOT NULL UNIQUE,
        balance DECIMAL(15, 2) NOT NULL,
        currency VARCHAR(3) NOT NULL,
        status VARCHAR(20) NOT NULL,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        CONSTRAINT pk_wallets PRIMARY KEY (id)
    );

    CREATE INDEX idx_wallets_wallet_id ON wallets (wallet_id);
    CREATE INDEX idx_wallets_merchant_id ON wallets (merchant_id);
END;

-- Create wallet_transactions table
IF OBJECT_ID('wallet_transactions', 'U') IS NULL
BEGIN
    CREATE TABLE wallet_transactions (
        id BIGINT IDENTITY(1,1) NOT NULL,
        reference VARCHAR(50) NOT NULL UNIQUE,
        transaction_reference VARCHAR(50) NOT NULL UNIQUE,
        wallet_id VARCHAR(10) NOT NULL,
        amount DECIMAL(15, 2) NOT NULL,
        currency VARCHAR(3) NOT NULL,
        wallet_action VARCHAR(20) NOT NULL,
        status VARCHAR(20) NOT NULL,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        CONSTRAINT pk_wallet_transactions PRIMARY KEY (id),
        CONSTRAINT fk_wallet_transactions_wallet_id
            FOREIGN KEY (wallet_id) REFERENCES wallets(wallet_id)
    );

    CREATE INDEX idx_wallet_transactions_wallet_id ON wallet_transactions (wallet_id);
    CREATE INDEX idx_wallet_transactions_reference ON wallet_transactions (reference);
    CREATE INDEX idx_wallet_transactions_wallet_action ON wallet_transactions (wallet_action);
    CREATE INDEX idx_wallet_transactions_transaction_reference ON wallet_transactions (transaction_reference);
END;
