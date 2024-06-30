drop table attendance;

rename table student_timeline to attendance;

alter table attendance
    change student_id user_id int null;