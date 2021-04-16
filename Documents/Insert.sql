# La password è il nome in minuscolo
INSERT INTO `ecommerce`.`user` VALUES (1, 'Mirko', 'Andena', 'mirko.andena@polimi.it', '6eb44ff6ac59c9b9d39e7ac203910f0384e4c2d3a59c8bfd7fecb7a5f6de1e10', 'Via Roma 4, Piacenza');
INSERT INTO `ecommerce`.`user` VALUES (2, 'dev', 'eloper', 'dev@tiw', 'ef260e9aa3c673af240d17a2660480361a8e081d1ffeca2a5ed0e3219fc18567', 'Via Qualcosa');
INSERT INTO `ecommerce`.`user` VALUES (3, 'Luca', 'Andena', 'luca.andena@gmail.com', 'd70f47790f689414789eeff231703429c7f88a10210775906460edbf38589d90', 'Via Roma 4, Piacenza');
INSERT INTO `ecommerce`.`user` VALUES (4, 'Pietro', 'Rossi', 'pietro.rossi@tiscali.it', '09c12d01508bb6b857cf2f84199fb3134b58985806b40d6440a1acf453b2e106', 'Via Perfetti 21, Parma');

INSERT INTO `ecommerce`.`seller` VALUES (1, 'Simone Verdi', 3, 40);
INSERT INTO `ecommerce`.`seller` VALUES (2, 'Marco Rossi', 1, null);
INSERT INTO `ecommerce`.`seller` VALUES (3, 'Marco Bianchi', 5, 10);

INSERT INTO `ecommerce`.`shipment_range` VALUES (1, 3, 15, 1); # da 1 a 3 => €15
INSERT INTO `ecommerce`.`shipment_range` VALUES (2, 10, 20, 1); # da 4 a 10 => €20, oltre boh
INSERT INTO `ecommerce`.`shipment_range` VALUES (3, 5, 4.50, 2); # da 1 a 5 => €4.50
INSERT INTO `ecommerce`.`shipment_range` VALUES (4, 7, 6.25, 2); # da 6 a 7 => €6.25
INSERT INTO `ecommerce`.`shipment_range` VALUES (5, null, 8.90, 2); # da 8 in su => €8.90
INSERT INTO `ecommerce`.`shipment_range` VALUES (6, 2, 3.30, 3); # da 1 a 2 => €3.30, oltre boh

INSERT INTO `ecommerce`.`category` VALUES (1, 'Tecnology');
INSERT INTO `ecommerce`.`category` VALUES (2, 'House');
INSERT INTO `ecommerce`.`category` VALUES (3, 'Sport');

INSERT INTO `ecommerce`.`article` VALUES (1, 'Computer', 'A fantastic computer very fast and easy to use', 'computer.png', 1);
INSERT INTO `ecommerce`.`article` VALUES (2, 'Tablet', 'Not the best but most beautiful', 'tablet.png', 1);
INSERT INTO `ecommerce`.`article` VALUES (3, 'Usb cable', 'The strongest cable in the world', 'usb.png', 1);
INSERT INTO `ecommerce`.`article` VALUES (4, 'The italian pot', 'Now you can cook the real Carbonara', 'pot.png', 2);
INSERT INTO `ecommerce`.`article` VALUES (5, 'Iron', 'Really? Do you want to use this?', 'iron.png', 2);
INSERT INTO `ecommerce`.`article` VALUES (6, 'Wooden chair', 'For indoors and outdoors, it can break under the rain', 'chair.png', 2);
INSERT INTO `ecommerce`.`article` VALUES (7, 'Ball', 'An amazing ball,\nsize: 4\"', 'ball.png', 3);
INSERT INTO `ecommerce`.`article` VALUES (8, 'Barbell row', 'Muscles?? Go to workout!', 'barbellrow.png', 3);
INSERT INTO `ecommerce`.`article` VALUES (9, 'Sport pants', 'For real men', 'pants.png', 3);