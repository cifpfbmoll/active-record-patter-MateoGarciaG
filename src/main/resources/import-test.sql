DROP TABLE IF EXISTS Student;

CREATE TABLE Student
(
    id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
    name VARCHAR (255) NOT NULL,
    surname VARCHAR(255),
    date_birth DATE NOT NULL,
    -- VARCHAR(22) because the number must be indicate the country. example: +34 and the limit is 22 characters
    phone VARCHAR(22),
    PRIMARY KEY (id)
);
INSERT INTO Student (id, name, surname, date_birth, phone)
VALUES (1050, 'Mateo', 'Alvarez', '2005-06-05', '+34 666666666');
INSERT INTO Student (id, name, surname, date_birth, phone)
VALUES (2050, 'Will', 'Smith', '1999-06-17', '+34 677878997');