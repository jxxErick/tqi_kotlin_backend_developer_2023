CREATE TABLE carrinho
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    usuario_id       BIGINT                NULL,
    quantidade_itens BIGINT                NOT NULL,
    valor_total      DECIMAL               NOT NULL,
    CONSTRAINT pk_carrinho PRIMARY KEY (id)
);



CREATE TABLE usuario
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    email       VARCHAR(255)          NOT NULL,
    nome        VARCHAR(255)          NOT NULL,
    cpf         VARCHAR(255)          NOT NULL,
    carrinho_id BIGINT                NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id)
);



CREATE TABLE venda
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    usuario_id         BIGINT                NULL,
    valor_total        DECIMAL               NOT NULL,
    forma_de_pagamento VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_venda PRIMARY KEY (id)
);

CREATE TABLE item_carrinho
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    produto_id     BIGINT                NULL,
    quantidade     BIGINT                NOT NULL,
    preco_unitario DECIMAL               NOT NULL,
    carrinho_id    BIGINT                NULL,
    CONSTRAINT pk_itemcarrinho PRIMARY KEY (id)
);

CREATE TABLE item_vendido
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    produto_id     BIGINT                NULL,
    quantidade     BIGINT                NOT NULL,
    preco_unitario DECIMAL               NOT NULL,
    venda_id       BIGINT                NULL,
    CONSTRAINT pk_itemvendido PRIMARY KEY (id)
);

ALTER TABLE item_vendido
    ADD CONSTRAINT FK_ITEMVENDIDO_ON_PRODUTO FOREIGN KEY (produto_id) REFERENCES produto (id);

ALTER TABLE item_vendido
    ADD CONSTRAINT FK_ITEMVENDIDO_ON_VENDA FOREIGN KEY (venda_id) REFERENCES venda (id);

ALTER TABLE item_carrinho
    ADD CONSTRAINT FK_ITEMCARRINHO_ON_CARRINHO FOREIGN KEY (carrinho_id) REFERENCES carrinho (id);

ALTER TABLE item_carrinho
    ADD CONSTRAINT FK_ITEMCARRINHO_ON_PRODUTO FOREIGN KEY (produto_id) REFERENCES produto (id);

ALTER TABLE venda
    ADD CONSTRAINT FK_VENDA_ON_USUARIO FOREIGN KEY (usuario_id) REFERENCES usuario (id);

ALTER TABLE carrinho
    ADD CONSTRAINT FK_CARRINHO_ON_USUARIO FOREIGN KEY (usuario_id) REFERENCES usuario (id);

ALTER TABLE usuario
    ADD CONSTRAINT FK_USUARIO_ON_CARRINHO FOREIGN KEY (carrinho_id) REFERENCES carrinho (id);

