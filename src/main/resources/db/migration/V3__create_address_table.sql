CREATE TABLE IF NOT EXISTS micro_user.tb_address (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES micro_user.tb_user(id),
    street VARCHAR(20),
    city VARCHAR(60),
    country VARCHAR(60),
    nomenclature VARCHAR(60),
    state VARCHAR(60),
    observation VARCHAR(500),
    zip_code VARCHAR(20)
);