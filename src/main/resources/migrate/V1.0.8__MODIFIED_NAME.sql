rename table student_course to student_curriculum;

alter table student_curriculum change course_id curriculum_id int null;

alter table subjects change course_id curriculum_id int null;

alter table schedules change teacher_id user_id int null;

alter table subjects
    add slots int null;

alter table users
    add description text null;

INSERT INTO curriculum (curriculum_name, description, status, created_at, created_by, modified_at, modified_by)
VALUES
('Software Engineering', 'Chương trình Kỹ thuật phần mềm', 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO curriculum (curriculum_name, description, status, created_at, created_by, modified_at, modified_by)
VALUES
('Information Assurance', 'Chương trình An toàn thông tin', 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO curriculum (curriculum_name, description, status, created_at, created_by, modified_at, modified_by)
VALUES
('Business Administration', 'Chương trình Quản trị kinh doanh', 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO curriculum (curriculum_name, description, status, created_at, created_by, modified_at, modified_by)
VALUES
('Digital Marketing', 'Chương trình Marketing số', 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO subjects (code, name, slots, status, created_at, created_by, modified_at, modified_by)
VALUES
('SE101', 'Introduction to Software Engineering', 45, 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO subjects (code, name, slots, status, created_at, created_by, modified_at, modified_by)
VALUES
('SE102', 'Object-Oriented Programming', 45, 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO subjects (code, name, slots, status, created_at, created_by, modified_at, modified_by)
VALUES
('SE103', 'Data Structures and Algorithms', 45, 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO subjects (code, name, slots, status, created_at, created_by, modified_at, modified_by)
VALUES
('SE104', 'Database Systems', 45, 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO subjects (code, name, slots, status, created_at, created_by, modified_at, modified_by)
VALUES
('SE105', 'Software Requirements Engineering', 45, 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO subjects (code, name, slots, status, created_at, created_by, modified_at, modified_by)
VALUES
('SE106', 'Software Design and Architecture', 45, 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO subjects (code, name, slots, status, created_at, created_by, modified_at, modified_by)
VALUES
('SE107', 'Software Testing and Quality Assurance', 45, 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO subjects (code, name, slots, status, created_at, created_by, modified_at, modified_by)
VALUES
('SE108', 'Software Project Management', 45, 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO subjects (code, name, slots, status, created_at, created_by, modified_at, modified_by)
VALUES
('SE109', 'Software Maintenance and Evolution', 45, 'ACTIVE', NOW(), 1, NOW(), 1);

INSERT INTO subjects (code, name, slots, status, created_at, created_by, modified_at, modified_by)
VALUES
('SE110', 'Software Engineering Capstone Project', 45, 'ACTIVE', NOW(), 1, NOW(), 1);

