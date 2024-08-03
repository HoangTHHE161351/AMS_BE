
CREATE TABLE notify (
    id INT AUTO_INCREMENT,
    message VARCHAR(200),  -- admin, staff, teacher, student
    room_id INT,
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read int  DEFAULT 0,
    PRIMARY KEY (id)
);

alter table notify
    add constraint notify_fb_1
        foreign key (room_id) references rooms (id);
