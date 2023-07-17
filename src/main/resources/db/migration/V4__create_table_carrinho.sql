CREATE TABLE carrinho
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    preco_total DOUBLE                NOT NULL,
    usuario_id  BIGINT                NULL,
    CONSTRAINT pk_carrinho PRIMARY KEY (id)
);

CREATE TABLE produto_carrinho
(
    carrinho_id BIGINT NOT NULL,
    produto_id  BIGINT NOT NULL
);

ALTER TABLE carrinho
    ADD CONSTRAINT FK_CARRINHO_ON_USUARIO FOREIGN KEY (usuario_id) REFERENCES usuario (id);

ALTER TABLE produto_carrinho
    ADD CONSTRAINT fk_procar_on_carrinho FOREIGN KEY (carrinho_id) REFERENCES carrinho (id);

ALTER TABLE produto_carrinho
    ADD CONSTRAINT fk_procar_on_produto FOREIGN KEY (produto_id) REFERENCES produto (i