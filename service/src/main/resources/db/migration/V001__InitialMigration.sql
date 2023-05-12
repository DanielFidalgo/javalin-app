CREATE TABLE items (
    id VARCHAR(40) NOT NULL,
    name VARCHAR(40) NOT NULL,
    quantity INT NOT NULL,
    created DATETIME NOT NULL,
    PRIMARY KEY(id)
)
ENGINE=InnoDB;