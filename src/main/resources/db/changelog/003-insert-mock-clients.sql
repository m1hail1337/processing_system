INSERT INTO clients (id, first_name, last_name, phone_number)
VALUES
('mock_user', 'Ivan', 'Ivanov', '+71234567890'),
('mock_operator', 'John', 'Snow', '+70987654321'),
('mock_admin', 'Gabe', 'Newell', '+71337133700');

INSERT INTO clients_roles (client_id, role)
VALUES
('mock_user', 'ROLE_USER'),
('mock_operator', 'ROLE_OPERATOR'),
('mock_admin', 'ROLE_ADMIN');

INSERT INTO users_auth (client_id, login, password)
VALUES
('mock_user', 'mihail', '12345'),
('mock_operator', 'stepan', '54321'),
('mock_admin', 'dasha', '00000');