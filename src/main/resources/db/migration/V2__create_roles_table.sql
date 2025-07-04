CREATE TABLE roles(
    id VARCHAR(36) PRIMARY KEY,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    role_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
); 