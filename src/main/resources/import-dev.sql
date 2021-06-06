DROP TABLE IF EXISTS Fruit;

CREATE TABLE Fruit
(
    id SERIAL NOT NULL,
    name VARCHAR (255) NOT NULL,
    description VARCHAR(255),
    PRIMARY KEY (id)
);
INSERT INTO Fruit (id, name, description)
VALUES (1050, 'Apple', 'Winter fruit');
INSERT INTO Fruit (id, name, description)
VALUES (2050, 'Pineapple', 'Tropical fruit');