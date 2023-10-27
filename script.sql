DROP TABLE IF EXISTS product_consumptions;
DROP TABLE IF EXISTS stocks;
DROP TABLE IF EXISTS stores;
CREATE TABLE stores (
                        id BIGINT AUTO_INCREMENT ,
                        name VARCHAR(255) NOT NULL,
                        location VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
);
CREATE TABLE stocks (
                        id BIGINT AUTO_INCREMENT,
                        store_id BIGINT NOT NULL,
                        product_id BIGINT NOT NULL,
                        quantity BIGINT NOT NULL,
                        data_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (store_id) REFERENCES stores(id),
                        PRIMARY KEY (id)
);
CREATE TABLE product_consumptions (
                                      id BIGINT AUTO_INCREMENT ,
                                      store_id BIGINT NOT NULL,
                                      product_id BIGINT NOT NULL,
                                      quantity_consumed BIGINT NOT NULL,
                                      data_consumed TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      FOREIGN KEY (store_id) REFERENCES stores(id),
                                      PRIMARY KEY (id)
);