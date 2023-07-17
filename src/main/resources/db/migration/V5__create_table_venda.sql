CREATE TABLE venda
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    valor_total        DOUBLE                NOT NULL,
    forma_de_pagamento VARCHAR(255)          NULL,
    carrinho_id        BIGINT                NULL,
    usuario_id         BIGINT                NULL,
    CONSTRAINT pk_venda PRIMARY KEY (id)
);

ALTER TABLE venda
    ADD CONSTRAINT FK_VENDA_ON_CARRINHO FOREIGN KEY (carrinho_id) REFERENCES carrinho (id);

ALTER TABLE venda
    ADD CONSTRAINT FK_VENDA_ON_USUARIO FOREIGN KEY (usuario_id) REFERENCES usuario (id);