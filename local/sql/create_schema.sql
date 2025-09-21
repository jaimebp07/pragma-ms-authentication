CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Crear esquema si no existe
CREATE SCHEMA IF NOT EXISTS credi_ya;

-- Crear tabla users
CREATE TABLE IF NOT EXISTS credi_ya.users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),                  -- Equivalente a String pero como UUID
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(20),
    email VARCHAR(150) UNIQUE NOT NULL,
    base_salary NUMERIC(15,2) NOT NULL,
    roles TEXT[] NOT NULL DEFAULT '{}',
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);
