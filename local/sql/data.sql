-- data for users table
INSERT INTO credi_ya.users (
    id, first_name, last_name, birth_date, address, phone_number, email, base_salary, roles, password
)
VALUES
    (
        '28390177-6796-465e-b002-dac9f0034df8',
        'Andres', 'Gomez', '1990-05-10',
        'Calle 123', '3001234567', 'andres@example.com',
        3500000.00,
        ARRAY['CLIENT'], -- roles como array
        '$2a$10$yfVHJzN6ldQNeCnwHDi9T.C79mkaGs.oyqvEMYITvby/0qL32bCB.'  -- hash de "123456"
    ),
    (
        gen_random_uuid(),
        'Maria', 'Lopez', '1985-11-20',
        'Carrera 45 #12-34', '3109876543', 'maria@example.com',
        4200000.50,
        ARRAY['ADVISOR'],
        '$2a$10$SLi7Fidu0lZ/LTqphGrBIOaEf90zaTVks2XSwx2wTRNTC9ntnwXpO'  -- hash de "123456"
    ),
    (
        gen_random_uuid(),
        'Raul', 'Lopez', '1985-11-20',
        'Carrera 45 #12-34', '3109876543', 'admin@example.com',
        4200000.50,
        ARRAY['ADMIN'],
        '$2a$10$LUcxdqz3oai5pyOtf97JieanlPjKTT0JDAZ26TVUmKWoMb/gyYTh.'  -- hash de "123456"
    ),
    ('9cf91033-f501-4abc-9ca8-6fe16757cc92', 'Laura',   'Martínez', '1992-04-15', 'Calle 10 #5-33', '3001112233', 'laura.martinez@example.com', 2800000.00, ARRAY['CLIENT'], '$2a$10$eTldV0pvdtWeg8o/7RlDYepSLebciKNQBaU9ALOSx1gejO76jsq5e'),
    ('0c6485b6-6b3a-4bdb-823e-0a8d71a3e7de', 'Carlos',  'Ramírez',  '1988-07-22', 'Av. 45 #20-10',  '3012223344', 'carlos.ramirez@example.com', 3200000.00, ARRAY['CLIENT'], '$2a$10$eTldV0pvdtWeg8o/7RlDYepSLebciKNQBaU9ALOSx1gejO76jsq5e'),
    ('3c59f39e-0a32-4d07-9a0b-fcd038e2e802', 'Paola',   'García',   '1995-11-02', 'Cra 12 #8-45',   '3023334455', 'paola.garcia@example.com',   2500000.50, ARRAY['CLIENT'], '$2a$10$eTldV0pvdtWeg8o/7RlDYepSLebciKNQBaU9ALOSx1gejO76jsq5e'),
    ('4e3f5b9b-8e5d-4a76-b394-2f1a6a3d5c7f', 'Rafaela',   'García',   '1995-11-02', 'Cra 12 #8-45',   '3023334455', 'rafaela.garcia@example.com',   7500000.50, ARRAY['CLIENT'], '$2a$10$eTldV0pvdtWeg8o/7RlDYepSLebciKNQBaU9ALOSx1gejO76jsq5e'),
    ('7b49d4a5-293a-4fcb-95f2-f0af59e196fd', 'Jorge',   'Pérez',    '1990-03-18', 'Calle 90 #15-20','3034445566', 'jorge.perez@example.com',    3000000.00, ARRAY['CLIENT'], '$2a$10$eTldV0pvdtWeg8o/7RlDYepSLebciKNQBaU9ALOSx1gejO76jsq5e'),
    ('93d75c8d-7414-4900-b5e5-ccf1c9e62a40', 'Natalia', 'Suárez',   '1993-06-11', 'Carrera 30 #4-8','3045556677', 'natalia.suarez@example.com', 3100000.75, ARRAY['CLIENT'], '$2a$10$eTldV0pvdtWeg8o/7RlDYepSLebciKNQBaU9ALOSx1gejO76jsq5e'),
    ('a6fa4c1e-6be7-4a6b-9391-82e646d92d60', 'Andrés',  'Moreno',   '1987-12-29', 'Av. Siempre Viva 123','3056667788','andres.moreno@example.com', 3400000.00, ARRAY['CLIENT'], '$2a$10$eTldV0pvdtWeg8o/7RlDYepSLebciKNQBaU9ALOSx1gejO76jsq5e'),
    ('b84a9f5a-8d4b-4c94-8466-9cb71241b772', 'Camila',  'Rojas',    '1991-09-05', 'Calle 25 #18-77','3067778899', 'camila.rojas@example.com',   2950000.00, ARRAY['CLIENT'], '$2a$10$eTldV0pvdtWeg8o/7RlDYepSLebciKNQBaU9ALOSx1gejO76jsq5e'),
    ('c3d2a55e-12f8-4b3a-82c7-2c9d174f91ee', 'Felipe',  'Castro',   '1989-01-14', 'Cra 50 #10-90',  '3078889900', 'felipe.castro@example.com',  2700000.00, ARRAY['CLIENT'], '$2a$10$eTldV0pvdtWeg8o/7RlDYepSLebciKNQBaU9ALOSx1gejO76jsq5e'),
    ('da187d87-1dc1-40fd-a2f0-3ac91a8f80fb', 'Diana',   'Vargas',   '1994-08-25', 'Calle 70 #9-66', '3089990011', 'diana.vargas@example.com',   3300000.25, ARRAY['CLIENT'], '$2a$10$eTldV0pvdtWeg8o/7RlDYepSLebciKNQBaU9ALOSx1gejO76jsq5e'),
    ('f5a31f6e-cd3f-44f7-bf11-0dbad81b5b59', 'Mateo',   'Torres',   '1996-02-17', 'Carrera 100 #22-33','3090001122','mateo.torres@example.com', 2600000.00, ARRAY['CLIENT'], '$2a$10$eTldV0pvdtWeg8o/7RlDYepSLebciKNQBaU9ALOSx1gejO76jsq5e');
    