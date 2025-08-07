CREATE TABLE ITEM_DO_PEDIDO(
    id BIGINT(20) NOT NULL auto_increment,
    descricao VARCHAR(255) DEFAULT NULL,
    quantidade INT(11) NOT NULL,
    pedido_id BIGINT(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id)
);