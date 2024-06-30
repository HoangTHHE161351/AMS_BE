
alter table cameras
    add user_name varchar(20) null;

alter table cameras
    add password varchar(32) null;


-- Create curriculum_sub table
CREATE TABLE curriculum_sub
(
    id            int AUTO_INCREMENT,
    curriculum_id INT,
    subject_id    INT,
    semester_id   int,
    status        VARCHAR(20),
    created_at    TIMESTAMP,
    created_by    INT,
    modified_at   TIMESTAMP,
    modified_by   int,
    PRIMARY KEY (id),
    FOREIGN KEY (curriculum_id) REFERENCES curriculum (id),
    FOREIGN KEY (subject_id) REFERENCES subjects (id),
    FOREIGN KEY (semester_id) REFERENCES semester (id)
);


