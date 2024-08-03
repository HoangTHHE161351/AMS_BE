CREATE TABLE facial
(
    id          INT AUTO_INCREMENT,
    userId      int,
    image       mediumblob,
    status      VARCHAR(20),
    created_at  TIMESTAMP,
    created_by  INT,
    modified_at TIMESTAMP,
    modified_by int,
    PRIMARY KEY (id)
);

create index idx_userId ON facial (userId);