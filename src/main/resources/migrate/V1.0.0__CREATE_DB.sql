-- Create database
DROP DATABASE IF EXISTS AMS;
CREATE DATABASE AMS;
USE AMS;
CREATE TABLE IF NOT EXISTS flyway_schema_history (
                                                     installed_rank INT NOT NULL,
                                                     version VARCHAR(50),
                                                     description VARCHAR(200) NOT NULL,
                                                     type VARCHAR(20) NOT NULL,
                                                     script VARCHAR(1000) NOT NULL,
                                                     checksum INT,
                                                     installed_by VARCHAR(100) NOT NULL,
                                                     installed_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                     execution_time INT NOT NULL,
                                                     success BOOLEAN NOT NULL
);

-- Create Roles table
CREATE TABLE roles (
    id INT AUTO_INCREMENT,
    role_name VARCHAR(50),  -- admin, staff, teacher, student
	status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    PRIMARY KEY (id)
);

-- create users table
create table users (
	id int AUTO_INCREMENT ,
	role_id INT,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(32),
    user_name varchar(20) UNIQUE,
	first_name NVARCHAR(50),
    last_name NVARCHAR(50),
	avata text,
    gender int,
    address NVARCHAR(200),
    phone VARCHAR(20),
    dob TIMESTAMP,
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    PRIMARY KEY (id),
	FOREIGN KEY (role_id) REFERENCES roles (id)
);

-- Create coures table
CREATE TABLE course (
    id INT AUTO_INCREMENT,
    course_name NVARCHAR(50),
    description NVARCHAR(255),
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    PRIMARY KEY (id)
);

-- Create Subjects table
CREATE TABLE subjects (
    id INT AUTO_INCREMENT,
    course_id INT,
    name NVARCHAR(255),
    code VARCHAR(50) UNIQUE,
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    PRIMARY KEY (id),
	FOREIGN KEY (course_id) REFERENCES course (id)
);

-- Create class_room table
CREATE TABLE class_room (
    id INT AUTO_INCREMENT,
    class_name VARCHAR(50),
    description NVARCHAR(255),
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    PRIMARY KEY (id)
);

-- Create TeacherSubs table
CREATE TABLE teacher_subject (
	id int AUTO_INCREMENT,
    teacher_id INT,
    subject_id INT,
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    PRIMARY KEY (id),
    FOREIGN KEY (subject_id) REFERENCES subjects (id),
    FOREIGN KEY (teacher_id) REFERENCES users (id)
);

-- Create StudentSubs table
CREATE TABLE student_class (
	id int AUTO_INCREMENT,
    student_id INT,
    class_id INT,
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    PRIMARY KEY (id),
    FOREIGN KEY (class_id) REFERENCES class_room (id),
    FOREIGN KEY (student_id) REFERENCES users (id)
);

CREATE table class_subject(
	id int primary key auto_increment,
	class_id int,
	subject_id int ,
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    FOREIGN KEY (class_id) REFERENCES class_room (id),
    FOREIGN KEY (subject_id) REFERENCES subjects (id)
);

-- Create Rooms table
CREATE TABLE rooms (
    id INT AUTO_INCREMENT,
    room_name NVARCHAR(50),
    description NVARCHAR(255),
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    PRIMARY KEY (id)
);

-- Create student_course table
CREATE TABLE student_course (
    id INT AUTO_INCREMENT,
    course_id int,
    student_id int,
    description NVARCHAR(255),
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    PRIMARY KEY (id),
    FOREIGN KEY (course_id) REFERENCES course (id),
    FOREIGN KEY (student_id) REFERENCES users (id)
);

-- Create TimeSlot table
CREATE TABLE time_slot (
    id INT AUTO_INCREMENT,
    start_time TIME,
    end_time TIME,
    description VARCHAR(50),
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    PRIMARY KEY (id)
);

-- Create Cameras table
CREATE TABLE cameras (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ip_tcpip VARCHAR(50),
    port	VARCHAR(30),
    description NVARCHAR(255),
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int
);

-- Create CameraRoom table
CREATE TABLE camera_room (
	id int PRIMARY KEy AUTO_INCREMENT, 
    camera_id INT,
    room_id INT,
    location NVARCHAR(255),
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    FOREIGN KEY (camera_id) REFERENCES cameras (id),
    FOREIGN KEY (room_id) REFERENCES rooms (id)
);

-- Create Attendance table
CREATE TABLE attendance (
	id int PRIMARY key AUTO_INCREMENT,
    student_id INT,
    subject_id INT,
	class_id INT,
    room_id INT,
    learn_TIMESTAMP TIMESTAMP,
    description NVARCHAR(255),
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    FOREIGN KEY (student_id) REFERENCES users (id),
    FOREIGN KEY (subject_id) REFERENCES subjects (id),
	FOREIGN KEY (class_id) REFERENCES class_room (id),
    FOREIGN KEY (room_id) REFERENCES rooms (id)
);

-- Create AccessLogs table
CREATE TABLE access_log (
    id INT primary key AUTO_INCREMENT,
    face_image BLOB,
	check_in timestamp,
	check_out timestamp,
    type int(1),
    student_id INT,
    account_id INT,
    is_glass int(1),
	is_mark int(1),
	is_beard int(1),
	similarity int(1),
    FOREIGN KEY (student_id) REFERENCES users (id)
);

-- create semester table
create table semester(
	id int primary key auto_increment,
    semester_name varchar(20),
    start_time timestamp,
    end_time timestamp,
	description text
);

-- Create Schedules table
CREATE TABLE schedules (
	id int primary key auto_increment,
    semester_id int,
    class_id INT,
    room_id INT,
    teacher_id INT,
    subject_id INT,
    time_slot_id INT,
    learn_timestamp TIMESTAMP,
    description NVARCHAR(255),
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    FOREIGN KEY (semester_id) REFERENCES semester (id),
    FOREIGN KEY (class_id) REFERENCES class_room (id),
    FOREIGN KEY (room_id) REFERENCES rooms (id),
    FOREIGN KEY (teacher_id) REFERENCES users (id),
    FOREIGN KEY (subject_id) REFERENCES subjects (id),
    FOREIGN KEY (time_slot_id) REFERENCES time_slot (id)
);

-- create student_timeline table
create table student_timeline(
	id int PRIMARY KEY AUTO_INCREMENT,
	student_id int ,
	schedule_id int,
    status VARCHAR(20),
	created_at TIMESTAMP,
	created_by INT,
	modified_at TIMESTAMP,
	modified_by int,
    FOREIGN KEY (student_id) REFERENCES users (id),
    FOREIGN KEY (schedule_id) REFERENCES schedules (id)
);

-- Create daily_log table
create table daily_log(
	id int primary key auto_increment,
    user_id int,
    activity_type VARCHAR(255) NOT NULL,
    activity_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_user_name ON users (user_name);
CREATE INDEX idx_email ON users (email);
CREATE INDEX idx_camera ON camera_room (camera_id);
CREATE INDEX idx_room ON camera_room (room_id);

-- Insert initial data into Roles
-- INSERT INTO roles (role_name)
-- VALUES  ('admin'),
-- 		('staft'),
-- 		('teacher'),
--         ('student');

-- -- Insert initial data into Accounts
-- INSERT INTO Accounts (UserName, Email, Password, RoleId, Active)
-- VALUES  ('admin','admin@gmail.com','12345678',1, 1), 
-- 		('tung','tung@gmail.com','12345678',3, 1),
--         ('duong','duong@gmail.com','12345678',2, 1);

-- -- Insert initial data into Subjects
-- INSERT INTO Subjects (SubName, SubCode)
-- VALUES ('Basic Cross-Platform Application Programming With .NET', 'PRN211'),
--        ('Mobile Programming', 'PRM392'),
--        ('SW Architecture and Design', 'SWD392');

-- -- Insert initial data into TimeSlot
-- INSERT INTO TimeSlot (StartTime, EndTime, Description)
-- VALUES ('07:30:00', '09:50:00', 'Slot 1'),
--        ('10:00:00', '12:20:00', 'Slot 2'),
--        ('12:50:00', '15:10:00', 'Slot 3'),
--        ('15:20:00', '17:40:00', 'Slot 4');

-- -- Insert initial data into Teachers
-- INSERT INTO Teachers (AccountId, TeacherCode)
-- VALUES (2, 'TungTD53');

-- -- Insert initial data into Rooms
-- INSERT INTO Rooms (RoomName)
-- VALUES ('DC201'), ('DC202'), ('DC203'), ('DC204'), ('D205'),
--        ('D206'), ('D207'), ('D208'), ('D209'), ('D210');

-- -- Insert initial data into class_room
-- INSERT INTO class_room (ClassName)
-- VALUES ('SE1610'), ('SE1611'), ('SE1612'), ('SE1613'), ('SE1614'),
--        ('JD1610'), ('JD1611'), ('JD1612'), ('JD1613'), ('JD1614');
