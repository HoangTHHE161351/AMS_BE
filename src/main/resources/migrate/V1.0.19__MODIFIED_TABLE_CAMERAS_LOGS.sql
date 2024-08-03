
alter table cameras
    add check_type varchar(20) null;

alter table access_log
    drop COLUMN room_id;

alter table access_log
    add camera_id int null;

alter table access_log
    add constraint access_log_fb_1
        foreign key (camera_id) references cameras (id);
