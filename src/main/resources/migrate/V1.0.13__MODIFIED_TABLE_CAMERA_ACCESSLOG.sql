ALTER TABLE cameras
    ADD channel_id INT NULL;

ALTER TABLE cameras
    DROP COLUMN user_name;

ALTER TABLE cameras
    DROP COLUMN password;


ALTER TABLE access_log
    DROP COLUMN is_glass;

ALTER TABLE access_log
    DROP COLUMN is_mark;

ALTER TABLE access_log
    DROP COLUMN is_beard;

ALTER TABLE access_log
    DROP COLUMN check_in;

ALTER TABLE access_log
    DROP COLUMN check_out;

ALTER TABLE access_log
DROP FOREIGN KEY access_log_ibfk_1;

ALTER TABLE access_log
    DROP COLUMN student_id;

ALTER TABLE access_log
    CHANGE account_id user_id INT NULL;

ALTER TABLE access_log
    ADD time_stamp TIMESTAMP;
