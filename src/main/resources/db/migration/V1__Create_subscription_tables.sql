-- Create subscription management tables

-- Subscription table
CREATE TABLE IF NOT EXISTS subscriptions (
    id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('CREATED', 'APPROVAL_PENDING', 'ACTIVE', 'SUSPENDED', 'CANCELLED', 'EXPIRED')),
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);