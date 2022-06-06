CREATE SCHEMA if NOT EXISTS testproject;

CREATE SEQUENCE if NOT EXISTS testproject.order_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;

CREATE SEQUENCE if NOT EXISTS testproject.item_id_seq
INCREMENT BY 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1;


CREATE TABLE IF NOT EXISTS testproject.order(
    id int8 NOT NULL DEFAULT nextval('testproject.order_id_seq'::regclass),
    price int4,
	quantity int4,
	CONSTRAINT order_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS testproject.item(
    id int8 NOT NULL DEFAULT nextval('testproject.item_id_seq'::regclass),
    item_name VARCHAR(255),
    price int4,
    order_id int8,
	CONSTRAINT item_pkey PRIMARY KEY (id),
	CONSTRAINT order_id_fkey FOREIGN KEY (order_id) REFERENCES testproject.item(id)
);


INSERT INTO testproject.item(id, item_name, price) VALUES (1, 'phone', 1000);
INSERT INTO testproject.item(id, item_name, price) VALUES (2, 'table', 2000);
INSERT INTO testproject.item(id, item_name, price) VALUES (3, 'computer', 3000)
