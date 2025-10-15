-- Create subscription management tables

-- Users' table (if not exists)
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(50) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true
);

-- Subscription table
CREATE TABLE IF NOT EXISTS subscriptions (
    id VARCHAR(50) PRIMARY KEY,
    paypal_subscription_id VARCHAR(255) NOT NULL UNIQUE,
    user_id VARCHAR(50) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    plan_id VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('CREATED', 'APPROVAL_PENDING', 'ACTIVE', 'SUSPENDED', 'CANCELLED', 'EXPIRED')),
    quantity VARCHAR(50) NOT NULL DEFAULT '1',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    start_time TIMESTAMP,
    next_billing_date TIMESTAMP,
    last_payment_date TIMESTAMP,
    cancelled_at TIMESTAMP,
    suspended_at TIMESTAMP,
    expired_at TIMESTAMP,
    custom_id VARCHAR(255),
    plan_name VARCHAR(255),
    plan_description TEXT,
    billing_amount DECIMAL(10,2),
    currency_code VARCHAR(3),
    billing_frequency VARCHAR(50)
);

-- Payment transactions table
CREATE TABLE IF NOT EXISTS payment_transactions (
    id VARCHAR(50) PRIMARY KEY,
    subscription_id VARCHAR(50) NOT NULL REFERENCES subscriptions(id) ON DELETE CASCADE,
    paypal_transaction_id VARCHAR(255) UNIQUE,
    paypal_payment_id VARCHAR(255),
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED', 'REFUNDED', 'PARTIALLY_REFUNDED')),
    amount DECIMAL(10,2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    payment_date TIMESTAMP,
    next_billing_date TIMESTAMP,
    failure_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fee_amount DECIMAL(10,2),
    net_amount DECIMAL(10,2),
    intent VARCHAR(100),
    payment_method VARCHAR(100),
    payer_info TEXT,
    billing_info TEXT
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_subscriptions_user_id ON subscriptions(user_id);
CREATE INDEX IF NOT EXISTS idx_subscriptions_paypal_id ON subscriptions(paypal_subscription_id);
CREATE INDEX IF NOT EXISTS idx_subscriptions_status ON subscriptions(status);
CREATE INDEX IF NOT EXISTS idx_subscriptions_next_billing ON subscriptions(next_billing_date);
CREATE INDEX IF NOT EXISTS idx_subscriptions_user_status ON subscriptions(user_id, status);

CREATE INDEX IF NOT EXISTS idx_payments_subscription_id ON payment_transactions(subscription_id);
CREATE INDEX IF NOT EXISTS idx_payments_paypal_transaction_id ON payment_transactions(paypal_transaction_id);
CREATE INDEX IF NOT EXISTS idx_payments_status ON payment_transactions(status);
CREATE INDEX IF NOT EXISTS idx_payments_date ON payment_transactions(payment_date);

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_active ON users(is_active);