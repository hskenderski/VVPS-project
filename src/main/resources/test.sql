INSERT INTO user VALUES(-999,'Hristiyan', 'Ivanov','Skenderski','hris@abv.bg','test','ADMIN',23,'FAMILY');
INSERT INTO user VALUES(-998,'Ivan','Ivanov','Ivanov','ivan@abv.bg','test','USER',15,'NO');
INSERT INTO user VALUES(-997,'Ivan','Ivanov','Ivanov','h.skenderski@abv.bg','test','USER',15,'PENSIONER');


INSERT INTO train VALUES (-999, 'TestGrad1', 'TestGrad2', '2023-01-01 12:00:00', '2023-01-01 14:00:00', 99, 99.99);
INSERT INTO train VALUES (-99 , 'TestGrad2', 'TestGrad1', '2023-01-01 12:00:00', '2023-01-01 14:00:00', 99, 99.99);

INSERT INTO train VALUES (-998, 'TestGrad1', 'TestGrad2', '2023-01-01 12:00:00', '2023-01-01 14:00:00', 99, 89.99);
INSERT INTO train VALUES (-997, 'TestGrad2', 'TestGrad3', '2023-01-01 11:00:00', '2023-01-01 13:00:00', 99, 89.99);
INSERT INTO train VALUES (-996, 'TestGrad4', 'TestGrad5', '2023-02-01 12:00:00', '2023-02-01 14:00:00', 99, 99.99);

INSERT INTO user_train(id,user_id, going_train_id, back_train_id, historized, cart, add_cart_date, price_with_discount, child)
VALUES(-11,(SELECT id FROM user WHERE email = 'ivan@abv.bg'),-999,-99,'N','Y',SYSDATE(),107.99,'Y');
INSERT INTO user_train(id,user_id, going_train_id, back_train_id, historized, cart, add_cart_date, price_with_discount, child)
VALUES(-12,(SELECT id FROM user WHERE email = 'ivan@abv.bg'),-999,-99,'N','Y',DATE_ADD(SYSDATE(),INTERVAL -8 DAY),107.99,'Y');
