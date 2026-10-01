-- -----------------------------------------------------
-- Database schema for Hibernate E-Commerce Project
-- Database: ecommerce_db
-- -----------------------------------------------------

CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

-- 1. Table: categories
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

-- 2. Table: products
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    stockQuantity INT NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_products_categories 
        FOREIGN KEY (category_id) REFERENCES categories(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 3. Table: users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role ENUM('ADMIN', 'CUSTOMER') NOT NULL
);

-- 4. Table: orders
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    orderDate DATE,
    totalAmount DOUBLE NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_orders_users 
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 5. Table: order_details
CREATE TABLE IF NOT EXISTS order_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity INT NOT NULL,
    unitPrice DOUBLE NOT NULL,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT fk_orderdetails_orders 
        FOREIGN KEY (order_id) REFERENCES orders(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_orderdetails_products 
        FOREIGN KEY (product_id) REFERENCES products(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Sample Reference Data
-- -----------------------------------------------------
INSERT INTO categories (id, name, description) VALUES 
(1, 'Electronics', 'Laptops, mobile devices, and accessories'),
(2, 'Books', 'Technical and non-technical publications');

INSERT INTO products (id, name, price, stockQuantity, category_id) VALUES 
(1, 'Gaming Laptop', 72999.00, 10, 1),
(2, 'Wireless Mouse', 1200.00, 50, 1),
(3, 'Java Persistence with Hibernate', 950.00, 30, 2);

INSERT INTO users (id, username, password, email, role) VALUES 
(1, 'admin_user', 'adminPass123', 'admin@ecommerce.com', 'ADMIN'),
(2, 'khushbu_k', 'pass1234', 'khushbu@example.com', 'CUSTOMER');