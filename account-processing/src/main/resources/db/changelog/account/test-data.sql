-- =====================
-- Таблица account
-- =====================
INSERT INTO accounts (id, client_id, product_id, balance, interest_rate, is_recalc, card_exist, status) VALUES
(1, 1001, 2001, 1000, 3.5, TRUE, TRUE, 'ACTIVE'),
(2, 1002, 2002, 2000, 2.8, TRUE, FALSE, 'ACTIVE'),
(3, 1003, 2003, 1500, 4.0, FALSE, TRUE, 'BLOCKED'),
(4, 1004, 2004, 3000, 1.5, TRUE, TRUE, 'ACTIVE'),
(5, 1005, 2005, 500, 3.0, FALSE, FALSE, 'CLOSED'),
(6, 1006, 2006, 750, 2.5, TRUE, TRUE, 'ARRESTED'),
(7, 1007, 2007, 1250, 3.7, FALSE, TRUE, 'ACTIVE'),
(8, 1008, 2008, 1800, 2.2, TRUE, FALSE, 'CLOSED'),
(9, 1009, 2009, 2200, 3.1, TRUE, TRUE, 'BLOCKED'),
(10, 1010, 2010, 900, 4.2, FALSE, TRUE, 'ACTIVE');

-- =====================
-- Таблица card
-- =====================
INSERT INTO cards (id, account_id, card_id, payment_system, status) VALUES
(1, 1, '1111222233330001', 'VISA', 'ACTIVE'),
(2, 2, '1111222233330002', 'MASTERCARD', 'BLOCKED'),
(3, 3, '1111222233330003', 'VISA', 'ACTIVE'),
(4, 4, '1111222233330004', 'MASTERCARD', 'ACTIVE'),
(5, 5, '1111222233330005', 'VISA', 'CANCELLED'),
(6, 6, '1111222233330006', 'MASTERCARD', 'BLOCKED'),
(7, 7, '1111222233330007', 'VISA', 'ACTIVE'),
(8, 8, '1111222233330008', 'MASTERCARD', 'ACTIVE'),
(9, 9, '1111222233330009', 'VISA', 'CANCELLED'),
(10, 10, '1111222233330010', 'MASTERCARD', 'ACTIVE');

-- =====================
-- Таблица payment
-- =====================
INSERT INTO payments (id, account_id, payment_date, amount, is_credit, payed_at, type) VALUES
(1, 1, '2025-09-01', 500, TRUE, '2025-09-01', 'DEPOSIT'),
(2, 2, '2025-09-02', 1000, FALSE, '2025-09-02', 'WITHDRAW'),
(3, 3, '2025-09-03', 250, TRUE, '2025-09-03', 'DEPOSIT'),
(4, 4, '2025-09-04', 750, FALSE, '2025-09-04', 'WITHDRAW'),
(5, 5, '2025-09-05', 1200, TRUE, '2025-09-05', 'DEPOSIT'),
(6, 6, '2025-09-06', 600, FALSE, '2025-09-06', 'PAYMENT'),
(7, 7, '2025-09-07', 300, TRUE, '2025-09-07', 'DEPOSIT'),
(8, 8, '2025-09-08', 400, FALSE, '2025-09-08', 'WITHDRAW'),
(9, 9, '2025-09-09', 900, TRUE, '2025-09-09', 'DEPOSIT'),
(10, 10, '2025-09-10', 800, FALSE, '2025-09-10', 'PAYMENT');

-- =====================
-- Таблица transaction
-- =====================
INSERT INTO transactions (id, account_id, card_id, type, amount, status, timestamp) VALUES
(1, 1, 1, 'DEPOSIT', 500, 'COMPLETE', '2025-09-01T10:00:00'),
(2, 2, 2, 'WITHDRAW', 1000, 'PROCESSING', '2025-09-02T11:00:00'),
(3, 3, 3, 'PAYMENT', 250, 'ALLOWED', '2025-09-03T12:00:00'),
(4, 4, 4, 'TRANSFER', 750, 'COMPLETE', '2025-09-04T13:00:00'),
(5, 5, 5, 'DEPOSIT', 1200, 'BLOCKED', '2025-09-05T14:00:00'),
(6, 6, 6, 'PAYMENT', 600, 'CANCELLED', '2025-09-06T15:00:00'),
(7, 7, 7, 'TRANSFER', 300, 'COMPLETE', '2025-09-07T16:00:00'),
(8, 8, 8, 'DEPOSIT', 400, 'ALLOWED', '2025-09-08T17:00:00'),
(9, 9, 9, 'WITHDRAW', 900, 'COMPLETE', '2025-09-09T18:00:00'),
(10, 10, 10, 'PAYMENT', 800, 'PROCESSING', '2025-09-10T19:00:00');
