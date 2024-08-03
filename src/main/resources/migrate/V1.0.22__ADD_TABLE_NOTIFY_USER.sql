ALTER TABLE notify
DROP COLUMN is_read;


CREATE TABLE notify_user (
     id INT AUTO_INCREMENT,
     notify_id INT,
     user_id INT,
     is_read INT DEFAULT 0,
     PRIMARY KEY (id)
);
