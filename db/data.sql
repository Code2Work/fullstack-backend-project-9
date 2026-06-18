INSERT INTO customers (customer_id, customer_name, email, country, signup_date) VALUES
(1, 'Ali Veli', 'ali.veli@example.com', 'Turkey', '2022-01-10'),
(2, 'Ayşe Yılmaz', 'ayse.yilmaz@example.com', 'Turkey', '2021-05-03'),
(3, 'John Doe', 'john.doe@example.com', 'USA', '2020-11-15'),
(4, 'Emma Brown', 'emma.b@example.co.uk', 'UK', '2023-02-21'),
(5, 'Carlos Mendez', 'carlos.m@example.com', 'Mexico', '2022-07-12'),
(6, 'Merve Demir', 'merve.d@example.com', 'Turkey', '2021-09-30');
INSERT INTO products (product_id, product_name, price, stock_quantity) VALUES
(1, 'Wireless Mouse', 199.90, 30),
(2, 'Gaming Keyboard', 499.99, 15),
(3, 'USB-C Cable', 49.95, 100),
(4, '27" Monitor', 1899.00, 8),
(5, 'Laptop Stand', 329.50, 20),
(6, 'Noise-Cancelling Headphones', 1250.75, 5);
INSERT INTO orders (order_id, customer_id, order_date, total_amount) VALUES
(1, 1, '2022-01-15', 249.90),
(2, 2, '2022-06-10', 499.99),
(3, 3, '2023-01-05', 1250.75),
(4, 1, '2023-03-12', 199.90),
(5, 4, '2023-05-01', 329.50),
(6, 5, '2023-06-18', 1899.00),
(7, 6, '2021-10-10', 49.95);
