CREATE TABLE clients (
    id VARCHAR(40) PRIMARY KEY,
    first_name VARCHAR(40),
    last_name VARCHAR(40),
    phone_number VARCHAR(12)
);

CREATE TABLE applications (
    id SERIAL PRIMARY KEY,
    status VARCHAR(10),
    message TEXT,
    client_id VARCHAR(40) REFERENCES clients(id) ON DELETE CASCADE,
    creation_date TIMESTAMP,
    CONSTRAINT status_check CHECK (status IN ('SKETCH', 'SENT', 'ACCEPTED', 'DECLINED'))
);

CREATE TABLE roles (
    role VARCHAR(20) PRIMARY KEY
);

CREATE TABLE clients_roles (
    client_id VARCHAR(40) REFERENCES clients(id) ON DELETE CASCADE,
    role VARCHAR(20) REFERENCES roles(role),
    PRIMARY KEY (client_id, role)
);

CREATE TABLE users_auth (
    client_id VARCHAR(40) REFERENCES clients(id) ON DELETE CASCADE PRIMARY KEY,
    login VARCHAR(40) UNIQUE,
    password VARCHAR(40)
);

CREATE TABLE auth_tokens (
    token VARCHAR(2048) PRIMARY KEY,
    type VARCHAR(40),
    creation_time TIMESTAMP,
    revoked BOOLEAN,
    user_id VARCHAR(40) REFERENCES users_auth(client_id) ON DELETE CASCADE
);

