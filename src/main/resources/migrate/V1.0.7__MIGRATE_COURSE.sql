rename table course to curriculum;

alter table curriculum
    change course_name curriculum_name varchar(50) charset utf8mb3;

alter table users modify avata mediumblob null;

alter table users
    add feature_face MEDIUMBLOB null;

alter table semester
    add status VARCHAR(20) null;

alter table semester
    add created_at TIMESTAMP null;

alter table semester
    add created_by INT null;

alter table semester
    add modified_at TIMESTAMP null;

alter table semester
    add modified_by int null;