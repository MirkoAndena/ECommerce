CREATE TABLE Users (
	id integer auto_increment primary key,
	name varchar(30) not null,
	surname varchar(30) not null,
	email varchar(30) not null,
	password varchar(30) not null,
	address varchar(30) not null
);

CREATE TABLE Seller (
	id integer auto_increment primary key,
	name varchar(30) not null,
	rating integer not null,
    free_shipping_threshold float not null,
    min_articles integer not null,
    max_articles integer not null,
    price float not null
);