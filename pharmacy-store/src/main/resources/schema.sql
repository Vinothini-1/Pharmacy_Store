CREATE TABLE medicines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    manufacturer VARCHAR(255),
    price DECIMAL(10,2),
    stock_quantity INT,
    expiry_date DATE,
    category VARCHAR(100),
    prescription_required BOOLEAN,
    batch_number VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
