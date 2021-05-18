use moviedb;
CREATE TABLE IF NOT EXISTS movies (
    id varchar(10) NOT NULL,
    title TEXT NOT NULL,
    year INTEGER NOT NULL,
    director varchar(100) NOT NULL,
    PRIMARY KEY (id),
    FULLTEXT(title)
);

CREATE TABLE IF NOT EXISTS stars (
    id varchar(10) NOT NULL,
    name varchar(100) NOT NULL,
    birthYear int,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS stars_in_movies (
    starId varchar(10) NOT NULL,
    FOREIGN KEY(starId) REFERENCES stars(id),
    movieId varchar(10) NOT NULL,
    FOREIGN KEY(movieId) REFERENCES movies(id)
);

CREATE TABLE IF NOT EXISTS genres (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(32) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS genres_in_movies (
    genreId int NOT NULL,
    FOREIGN KEY(genreId) REFERENCES genres(id),
    movieId varchar(10) NOT NULL,
    FOREIGN KEY(movieId) REFERENCES movies(id)
);

CREATE TABLE IF NOT EXISTS creditcards(
    id VARCHAR(20) NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    expiration DATE NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS customers (
    id int NOT NULL AUTO_INCREMENT,
    firstName varchar(50) NOT NULL,
    lastName varchar(50) NOT NULL,
    ccId varchar(20)  NOT NULL,
    FOREIGN KEY(ccId) REFERENCES creditcards(id),
    address varchar(200)  NOT NULL,
    email varchar(50)  NOT NULL,
    password varchar(20)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sales (
    id int NOT NULL AUTO_INCREMENT,
    customerID int NOT NULL,
    FOREIGN KEY(customerID) REFERENCES customers(id),
    movieId varchar(10) NOT NULL,
    FOREIGN KEY(movieId) REFERENCES movies(id),
    saleDate date NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS sales (
    id INT NOT NULL AUTO_INCREMENT,
    customerId INT NOT NULL,
    FOREIGN KEY(customerId) REFERENCES customers(id),
    movieId VARCHAR(10) NOT NULL,
    FOREIGN KEY(movieId) REFERENCES movies(id),
    saleDate DATE NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS ratings (
    movieId varchar(10) NOT NULL,
    FOREIGN KEY(movieId) REFERENCES movies(id),
    rating float NOT NULL,
    numVotes int NOT NULL
);


CREATE TABLE IF NOT EXISTS ratings(
    movieId VARCHAR(10) NOT NULL,
    FOREIGN KEY(movieId) REFERENCES movies(id),
    rating FLOAT NOT NULL,
    numVotes INT NOT NULL
);