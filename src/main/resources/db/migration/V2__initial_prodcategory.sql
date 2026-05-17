INSERT INTO categories (id, name) VALUES
                                      (1, 'Electronics'),
                                      (2, 'Clothing'),
                                      (3, 'Books'),
                                      (4, 'Home & Kitchen'),
                                      (5, 'Sports');

INSERT INTO products (name, description, price, category_id) VALUES
('iPhone 15', 'Apple smartphone latest model', 79999.00, 1),
('Samsung TV', '55 inch 4K Smart TV', 45999.00, 1),
('Bluetooth Headphones', 'Noise cancelling headphones', 2999.00, 1),
('Men T-Shirt', 'Cotton round neck T-shirt', 599.00, 2),
('Women Jeans', 'Slim fit blue jeans', 1299.00, 2),
('Jacket', 'Winter leather jacket', 3499.00, 2),
('Atomic Habits', 'Self improvement book', 499.00, 3),
('Clean Code', 'Programming best practices', 799.00, 3),
('Rich Dad Poor Dad', 'Personal finance book', 399.00, 3),
('Mixer Grinder', '750W kitchen mixer', 2499.00, 4),
('Office Chair', 'Ergonomic chair', 5999.00, 4),
('Cricket Bat', 'English willow bat', 3499.00, 5),
('Football', 'FIFA approved size 5', 899.00, 5);