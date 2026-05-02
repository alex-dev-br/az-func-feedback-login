CREATE TABLE tb_user (
    id UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL
);

INSERT INTO tb_user (id, username, password, user_type) VALUES 
(RANDOM_UUID(), 'aluno', 'senha123', 'ALUNO'),
(RANDOM_UUID(), 'admin', 'senha123', 'ADMIN'),
(RANDOM_UUID(), 'professor', 'senha123', 'PROFESSOR');
