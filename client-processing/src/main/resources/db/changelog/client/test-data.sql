-- ========================================
-- Сущность User
-- ========================================
INSERT INTO users (id, login, password, email) VALUES
(1, 'user1', 'pass1', 'user1@example.com'),
(2, 'user2', 'pass2', 'user2@example.com'),
(3, 'user3', 'pass3', 'user3@example.com'),
(4, 'user4', 'pass4', 'user4@example.com'),
(5, 'user5', 'pass5', 'user5@example.com'),
(6, 'user6', 'pass6', 'user6@example.com'),
(7, 'user7', 'pass7', 'user7@example.com'),
(8, 'user8', 'pass8', 'user8@example.com'),
(9, 'user9', 'pass9', 'user9@example.com'),
(10, 'user10', 'pass10', 'user10@example.com');

-- ========================================
-- Сущность Client
-- ========================================
INSERT INTO clients (id, "client_id", "user_id", "first_name", "middle_name", "last_name", "date_of_birth", "document_type", "document_id", "document_prefix", "document_suffix") VALUES
(1, '770100000001', 1, 'Ivan', 'Petrovich', 'Ivanov', '1980-01-15', 'PASSPORT', '123456', 'AA', '01'),
(2, '770100000002', 2, 'Petr', 'Sergeevich', 'Petrov', '1975-05-20', 'INT_PASSPORT', '987654', 'BB', '02'),
(3, '770100000003', 3, 'Anna', 'Ivanovna', 'Smirnova', '1990-12-10', 'PASSPORT', '456789', 'CC', '03'),
(4, '770100000004', 4, 'Olga', 'Petrovna', 'Kuznetsova', '1985-03-05', 'BIRTH_CERT', '112233', 'DD', '04'),
(5, '770100000005', 5, 'Dmitry', 'Nikolaevich', 'Sokolov', '1992-07-17', 'PASSPORT', '334455', 'EE', '05'),
(6, '770200000006', 6, 'Elena', 'Sergeevna', 'Fedorova', '1988-11-23', 'INT_PASSPORT', '556677', 'FF', '06'),
(7, '770200000007', 7, 'Alexey', 'Ivanovich', 'Morozov', '1979-09-30', 'PASSPORT', '778899', 'GG', '07'),
(8, '770200000008', 8, 'Marina', 'Petrovna', 'Nikolaeva', '1982-06-12', 'BIRTH_CERT', '998877', 'HH', '08'),
(9, '770200000009', 9, 'Sergey', 'Dmitrievich', 'Volkov', '1995-04-18', 'PASSPORT', '667788', 'II', '09'),
(10, '770200000010', 10, 'Tatiana', 'Alexeevna', 'Lebedeva', '1987-08-09', 'INT_PASSPORT', '445566', 'JJ', '10');

-- ========================================
-- Сущность Product
-- ========================================
INSERT INTO products (id, name, key, create_date, product_id) VALUES
(1, 'Debit Card', 'DC', '2020-01-01', 1),
(2, 'Credit Card', 'CC', '2020-02-01', 2),
(3, 'Account', 'AC', '2020-03-01', 3),
(4, 'Investment Plan', 'IPO', '2020-04-01', 4),
(5, 'Payment Card', 'PC', '2020-05-01', 5),
(6, 'Pension Plan', 'PENS', '2020-06-01', 6),
(7, 'Savings Account', 'NS', '2020-07-01', 7),
(8, 'Insurance', 'INS', '2020-08-01', 8),
(9, 'Bank Service', 'BS', '2020-09-01', 9),
(10, 'Credit Card Premium', 'CC', '2020-10-01', 10);

-- ========================================
-- Сущность ClientProduct
-- ========================================
INSERT INTO client_products (id, client_id, product_id, open_date, close_date, status) VALUES
(1, '770100000001', 1, '2021-01-01', NULL, 'ACTIVE'),
(2, '770100000002', 2, '2021-02-01', '2023-01-01', 'CLOSED'),
(3, '770100000003', 3, '2021-03-01', NULL, 'ACTIVE'),
(4, '770100000004', 4, '2021-04-01', NULL, 'BLOCKED'),
(5, '770100000005', 5, '2021-05-01', '2022-12-31', 'CLOSED'),
(6, '770200000006', 6, '2021-06-01', NULL, 'ACTIVE'),
(7, '770200000007', 7, '2021-07-01', NULL, 'ARRESTED'),
(8, '770200000008', 8, '2021-08-01', NULL, 'ACTIVE'),
(9, '770200000009', 9, '2021-09-01', NULL, 'ACTIVE'),
(10, '770200000010', 10, '2021-10-01', NULL, 'BLOCKED');
