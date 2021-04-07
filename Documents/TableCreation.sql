USE ecommerce;

CREATE TABLE Users (
	id integer auto_increment primary key,
	name varchar(30),
	surname varchar(30),
	email varchar(30),
	password varchar(30),
	address varchar(30)
);

CREATE TABLE Sellers (
	id integer auto_increment primary key,
	name varchar(30),
	rating integer,
    free_shipping_threshold float,
    min_articles integer,
    max_articles integer,
    price float
);

CREATE TABLE Articles (
	id integer auto_increment primary key,
    name varchar(30),
    description varchar(200),
    image varchar(100),
    category varchar(30)
);

CREATE TABLE Seles (
	id integer auto_increment primary key,
    seller integer,
    article integer,
    price float,
    foreign key (seller) references Sellers(id),
    foreign key (article) references Articles(id)
);

CREATE TABLE Orders (
	id integer auto_increment primary key,
    total float,
    shipment_date date,
	seller integer,
    user_address varchar(30),
    user integer,
    foreign key (seller) references Sellers(id),
    foreign key (user) references Users(id)
);

CREATE TABLE OrderContainer (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    order_id INTEGER,
    article INTEGER,
    FOREIGN KEY (order_id)
        REFERENCES Orders (id),
    FOREIGN KEY (article)
        REFERENCES Articles (id)
);