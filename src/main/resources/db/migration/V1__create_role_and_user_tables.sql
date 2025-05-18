CREATE TABLE micro_user.tb_role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE micro_user.tb_user (
    id BIGSERIAL PRIMARY KEY,
    dni VARCHAR(20) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20),
    password TEXT NOT NULL,
    status VARCHAR(50),
    role_id BIGINT REFERENCES micro_user.tb_role(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);
