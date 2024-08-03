alter table attendance
    add semester_id int null;

alter table attendance
    add constraint attendance_ibfk_5
        foreign key (semester_id) references semester (id);

alter table attendance
    change student_id user_id int null;

alter table student_timeline
    add description nvarchar(255) null;

alter table cameras
    change ip_tcpip ip_tcip varchar(50) null;

create index idx_class_name
    on class_room (class_name);

create index idx_room_name
    on rooms (room_name);

create index idx_semester_name
    on semester (semester_name);

alter table subjects
    drop foreign key subjects_ibfk_1;

drop index course_id on subjects;

alter table subjects
    add constraint subjects_ibfk_1
        foreign key (curriculum_id) references curriculum (id);

create index idx_subject_code
    on subjects (code);

create index idx_curriculum_name
    on curriculum (curriculum_name);

INSERT IGNORE INTO roles (role_name, status, created_at)
VALUES ('STUDENT', 'ACTIVE', now());


