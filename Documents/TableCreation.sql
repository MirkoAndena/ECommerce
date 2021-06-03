CREATE SCHEMA `ecommerce`;

CREATE TABLE `ecommerce`.`user` (
	`id` int not null auto_increment primary key,
	`name` varchar(30) not null,
	`surname` varchar(30) not null,
	`email` varchar(30) not null,
	`password` char(64) not null,
	`address` varchar(30) not null
);

CREATE TABLE `ecommerce`.`seller` (
	`id` int not null auto_increment primary key,
	`name` varchar(30) not null,
	`rating` int not null CHECK (`rating` >= 0 AND `rating` <= 5),
    `free_shipping_threshold` float CHECK(`free_shipping_threshold` > 0) 
    # Non ha senso indicare la consegna gratuita qua (free_shipping_threshold = 0) se poi
    # ci sono delle entry in shipment_range (per consegna gratuita mettere range con max=null e price=0))
);

CREATE TABLE `ecommerce`.`shipment_range` (
	`id` int not null auto_increment primary key,
	`max_articles` int,
    `price` float not null,
    `seller` int not null,
    foreign key (`seller`) references `ecommerce`.`seller`(`id`)
);

CREATE TABLE `ecommerce`.`category` (
	`id` int not null auto_increment primary key,
    `name` varchar(30) not null
);

CREATE TABLE `ecommerce`.`article` (
	`id` int not null auto_increment primary key,
    `name` varchar(30) not null,
    `description` varchar(200) not null,
    `image` varchar(100) not null,
    `category` int not null,
    foreign key (`category`) references `ecommerce`.`category`(`id`)
);

CREATE TABLE `ecommerce`.`articles_seen` (
	`id` int not null auto_increment primary key,
    `user` int not null,
    `article` int not null,
    `datetime` bigint not null,
    foreign key (`article`) references `ecommerce`.`article`(`id`),
    foreign key (`user`) references `ecommerce`.`user`(`id`)
);

CREATE TABLE `ecommerce`.`seller_articles` (
	`id` int not null auto_increment primary key,
    `seller` int not null,
    `article` int not null,
    `price` float not null,
    foreign key (`seller`) references `ecommerce`.`seller`(`id`),
    foreign key (`article`) references `ecommerce`.`article`(`id`)
);

CREATE TABLE `ecommerce`.`order` (
	`id` int not null auto_increment primary key,
    `total` float not null,
    `shipment_date` date not null,
	`seller` int not null,
    `user_address` varchar(30) not null,
    `user` int not null,
    foreign key (`seller`) references `ecommerce`.`seller`(`id`),
    foreign key (`user`) references `ecommerce`.`user`(`id`)
);

CREATE TABLE `ecommerce`.`order_articles` (
	`id` int not null auto_increment primary key,
    `order` int not null,
    `article` int not null,
    foreign key (`order`) references `ecommerce`.`order`(`id`),
    foreign key (`article`) references `ecommerce`.`article`(`id`)
);