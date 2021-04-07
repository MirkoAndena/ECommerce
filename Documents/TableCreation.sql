CREATE TABLE 'ecommerce'.'user' (
	'id' int not null auto_increment primary key,
	'name' varchar(30) not null,
	'surname' varchar(30) not null,
	'email' varchar(30) not null,
	'password' varchar(30) not null,
	'address' varchar(30) not null
);

CREATE TABLE 'ecommerce'.'seller' (
	'id' int not null auto_increment primary key,
	'name' varchar(30) not null,
	'rating' int not null,
    'free_shipping_threshold' float not null,
    'min_articles' int not null,
    'max_articles' int not null,
    'price' float not null
);

CREATE TABLE 'ecommerce'.'article' (
	'id' int not null auto_increment primary key,
    'name' varchar(30) not null,
    'description' varchar(200) not null,
    'image' varchar(100) not null,
    'category' varchar(30) not null
);

CREATE TABLE 'ecommerce'.'seller_articles' (
	'id' int not null auto_increment primary key,
    'seller' int not null,
    'article' int not null,
    'price' float not null,
    foreign key ('seller') references 'ecommerce'.'seller'('id'),
    foreign key ('article') references 'ecommerce'.'article'('id')
);

CREATE TABLE 'ecommerce'.'order' (
	'id' int not null auto_increment primary key,
    'total' float not null,
    'shipment_date' date not null,
	'seller' int not null,
    'user_address' varchar(30) not null,
    'user' int not null,
    foreign key ('seller') references 'ecommerce'.'seller'('id'),
    foreign key ('user') references 'ecommerce'.'user'('id')
);

CREATE TABLE 'ecommerce'.'order_articles' (
	'id' int not null auto_increment primary key,
    'order' int not null,
    'article' int not null,
    foreign key ('order') references 'ecommerce'.'order'('id'),
    foreign key ('article') references 'ecommerce'.'article'('id')
);