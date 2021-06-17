//TODO: this is not enough? probably
DROP TABLE IF EXISTS COFFEE;
CREATE TABLE COFFEE
(
    `id`                 LONG IDENTITY NOT NULL PRIMARY KEY,
    `manufactureCountry` VARCHAR(3)    NOT NULL,
    `acidity`            FLOAT,
    `collectionDate`     DATE          NOT NULL,
    `roastDate`          DATE          NOT NULL,
    `deliveryCountry`    VARCHAR(3)    NOT NULL,
    `name`               VARCHAR(12)   NOT NULL,
    `roastingDepth`      INT
);
