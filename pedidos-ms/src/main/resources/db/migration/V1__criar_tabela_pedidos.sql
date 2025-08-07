CREATE TABLE PEDIDOS(
    id BIGINT(20) NOT NULL auto_increment,
    data_hora DATETIME NOT NULL,
    status VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);