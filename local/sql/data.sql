-- data for users table
INSERT INTO credi_ya.users (
    id, first_name, last_name, birth_date, address, phone_number, email, base_salary, roles, password
)
VALUES
    (
        gen_random_uuid(),
        'Andres', 'Gomez', '1990-05-10',
        'Calle 123', '3001234567', 'andres@example.com',
        3500000.00,
        ARRAY['CLIENT'], -- roles como array
        '$2a$10$W7sYfQvJpT9V1nKfYwFfhe8gY.xcvK0hWYt9X3QF5.qnUVRtxlIz6' -- hash de "123456"
    ),
    (
        gen_random_uuid(),
        'Maria', 'Lopez', '1985-11-20',
        'Carrera 45 #12-34', '3109876543', 'maria@example.com',
        4200000.50,
        ARRAY['ADVISOR'],
        '$2a$10$KIXnDbP0vJo3Z9l7AqzMMeJcUODpF1aT1ybP7kMnnp7xFQxy38x1i' -- hash de "password"
    );