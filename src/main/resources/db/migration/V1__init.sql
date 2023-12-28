CREATE TABLE spisokticket
(
    id          serial primary key,
    passenger        varchar(100),
    route     varchar(250),
    price INTEGER
);

INSERT INTO spisokticket (passenger, route, price)
VALUES
       ('Прилуков В.А', 'Ледокол', 1000),
       ('Пассажеников И.Д', 'Ледокол', 7000),
       ('Петров П.П', 'Ледокол', 500),
       ('Ботев А.А', 'Ледокол', 4000),
       ('Бершат Р.О', 'Степь', 100),
       ('Перминов Р.О', 'Степь', 1000),
       ('Рыбов Р.О', 'Мель', 1060),
       ('Ащимов Р.О', 'Мель', 1600),
       ('Игнатьев Р.О', 'Поле', 1000),
       ('Иванов И.И', 'Степь', 1500);

CREATE TABLE users
(
    username VARCHAR(50)  NOT NULL,
    password VARCHAR(100) NOT NULL,
    name     VARCHAR(20)  NOT NULL,
    enabled  boolean      NOT NULL,
    PRIMARY KEY (username)
);

INSERT INTO users
VALUES ('admin', '$2a$10$dYJ9JcdxtCIc6jnJYNTDFOs1tdPt1te25Gf5JKIEc7uRBvJiSk6JO', 'gggg', true),
       ('natalya', '$2y$10$nObNno8RmbN3bAmzdR4QVOf0jtGtGaZlDkzMP9vxRbOtaFL6CbTZy', 'Супер', true),
       ('user', '$2a$10$zxVS3muLezmSlzipO76OVuUsEPwxBzgYrMMBXu.b383sFiaO.rB5m', 'Пользователь', true);

CREATE TABLE authorities
(
    username  varchar(50) NOT NULL,
    authority varchar(50) NOT NULL,

    CONSTRAINT authorities_idx UNIQUE (username, authority),

    CONSTRAINT authorities_ibfk_1
        FOREIGN KEY (username)
            REFERENCES users (username)
);

INSERT INTO authorities
VALUES ('admin', 'ROLE_ADMIN'),
       ('natalya', 'ROLE_ADMIN'),
       ('user', 'ROLE_USER');