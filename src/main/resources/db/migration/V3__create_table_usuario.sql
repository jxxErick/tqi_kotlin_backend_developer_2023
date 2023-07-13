CREATE TABLE usuario
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    email VARCHAR(255)          NULL,
    nome  VARCHAR(255)          NULL,
    cpf   VARCHAR(255)          NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id)
);