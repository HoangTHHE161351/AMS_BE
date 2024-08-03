
alter table cameras
    add username varchar(20) null;

alter table cameras
    add password varchar(32) null;

alter table cameras
    add camera_type varchar(20) null;
