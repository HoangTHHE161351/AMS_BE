package vn.attendance.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter

public enum MessageCode implements MessageCodeEnum {
    NOT_FOUND("Not Found"),
    ROLE_NOT_FOUND("Role Not Found"),
    USERNAME_NOT_FOUND("Username Not Found"),
    SCHEDULES_EXIST_FOR_TIME_SLOT("There are still schedules for this time slot."),
    SUBJECT_ALREADY_ASSIGNED_TO_TEACHER("The subject has already been assigned to the teacher."),
    SCHEDULES_EXIST_FOR_TEACHER("There are still schedules for this teacher."),
    SUBJECT_ALREADY_ADDED("The subject has already been added for the curriculum!"),
    TIMESLOT_ALREADY_ASSIGNED("Cannot edit time slot because schedules have already been assigned to it."),
    SCHEDULES_EXIST_FOR_ROOM("There are still schedules for this room."),
    SCHEDULES_EXIST_FOR_SUBJECT("There are still schedules for this subject."),
    CAMERAS_EXIST_FOR_ROOM("There are still cameras associated with this room."),
    SEND_EMAIL_FAIL("Failed to send the email!"),
    USER_NOT_FOUND("User Not Found"),
    SCHEDULE_PAST_TIMESTAMP("The schedule timestamp is in the past"),
    STUDENTS_ENROLLED_IN_CURRICULUM("There are still students enrolled in this curriculum!"),
    SEMESTER_END_TIME_HAS_ALREADY_PASSED("Cannot update semester end time because the end time has already passed."),
    SEMESTER_START_TIME_HAS_ALREADY_PASSED("The start time has already passed."),
    CURRICULUM_ALREADY_ADDED("The curriculum has already been added for the student!"),
    STUDENT_ALREADY_IN_CLASS("Student already in class!"),
    MQTT_DISCONNECT("Unable to connect to MQTT server"),
    SCHEDULE_FAILED_ROOM_SLOT("Room conflict detected for the same time slot"),
    SCHEDULE_FAILED_CLASS_SLOT("Class conflict detected for the same time slot"),
    SCHEDULE_FAILED_TEACHER_SLOT("Teacher conflict detected for the same time slot"),
    CAMERA_NOT_EVENT("The camera does not have a face comparison feature!"),
    LOGIN_FAIL("Username or password incorrect"),
    SUBJECT_MISMATCH("The student cannot be added to the class because there is no matching subject between the class and the student's curriculum."),
    OTP_EXPIRED("Opt is incorrect or expired!"),
    OLD_PASSWORD_NOT_MATCH("Old password not match with current password!"),
    NEW_PASSWORD_MATCH("New password cannot be the same as the old password!"),
    TOKEN_EXPIRE("Token expire!"),
    USERNAME_ALREADY_EXISTS("Username already exists!"),
    FACE_NOT_FOUND("The user has no facial data!"),
    EMAIL_ALREADY_EXISTS("Email already exists!"),
    DISK_STATE_FAIL("Unable to retrieve disk state!"),
    CAMERA_INFO_FAIL("Unable to retrieve camera information!"),
    SERVER_CONNECT_FAIL("Unable to connect to the server!"),
    EXPORT_FAIL("Export file failed!"),
    LOG_FAIL("Log failed!"),
    CONTENT_EMPTY("No data is returned!"),
    ADD_USER_FAIL("Add User Fail!"),
    ADD_CAMERA_FAIL("Add Camera Fail!"),
    USER_NOT_AUTHORITY("User not authority!"),
    FORMAT_FAIL("Format failed!"),
    SUBJECT_CODE_ALREADY_EXISTS("Subject code already exists."),
    SUBJECT_NAME_ALREADY_EXISTS("Subject name already exists!"),
    ADD_SUBJECT_FAIL("Add Subject Fail!"),
    ROOM_ALREADY_EXISTS("Room already exists!"),
    ROOM_NAME_ALREADY_EXISTS("Room name already exists!"),
    ADD_ROOM_FAIL("Add room fail"),
    ADD_CURRICULUM_FAIL("Add curriculum fail"),
    PHONE_ALREADY_EXISTS("Phone already exists!"),
    CAMERAROOM_ALREADY_EXISTED("Camera Room already exists!"),
    ADD_SEMESTER_FAIL("Add Semester Fail!"),
    ADD_TIMESLOT_FAIL("Add Slot Fail!"),
    ADD_CAMERAROOM_FAIL("Add Camera Room Fail!"),
    ADD_CLASSROOM_FAIL("Add ClassRoom Fail!"),
    UPDATE_SEMESTER_FAIL("Update Semester Fail!"),
    UPDATE_PROFILE_FAIL("Update Profile Fail!"),
    UPDATE_CURRICULUM_FAIL("Update Curriculum Fail!"),
    ADD_USER_FACE_FAIL("Add Face Fail!"),
    DELETE_USER_FACE_FAIL("Delete Face Fail!"),
    DELETE_CAMERA_FAIL("Delete Camera Fail!"),
    INVALID_TIME_RANGE("Invalid Time Range!"),
    SEMESTER_ALREADY_EXISTS("Semester already exists!"),
    TIMESLOT_TIME_ALREADY_EXISTS("Slot time already exists!"),
    TIMESLOT_NAME_ALREADY_EXISTS("Slot name already exists!"),
    SEMESTER_NAME_ALREADY_EXISTS("Semester name already exists!"),
    SCHEDULE_ALREADY_EXISTS("Schedule already exists!"),
    TIMESLOT_ALREADY_EXISTS("Timeslot already exists!"),
    CLASSNAME_ALREADY_EXISTED("Class already exists!"),
    CURRICULUM_NAME_EXIST("Curriculum name already exists!"),
    SCHEDULE_FAILED_SUBJECT("The schedule has a different subject!"),
    SCHEDULE_FAILED_CLASS("The schedule has a different class!"),
    SCHEDULE_FAILED_TEACHER("The schedule has a different teacher!"),
    SCHEDULE_FAILED_CLASS_TIME("The schedule has a different class in timeslot!"),
    ROOM_NOT_FOUND("Room not found!"),
    CLASS_NOT_FOUND("Class not found!"),
    TIMESLOT_NOT_FOUND("TimeSlot not found!"),
    SUBJECT_NOT_FOUND("Subject not found!"),
    CAMERAROOM_NOT_FOUND("Camera Room not found!"),
    CAMERA_NOT_FOUND("Camera not found!"),
    CLASSROOM_NOT_FOUND("Classroom not found!"),
    CURRICULUM_NOT_FOUND("Curriculum not found!"),
    LOG_NOT_FOUND("Log not found!"),
    CURRICULUMSUB_NOT_FOUND("Curriculum Subject Not Found"),
    CURRICULUMSUB_NO_CHANGES("No changes was made to this Curriculum Subject"),
    CURRICULUMSUB_ALREADY_EXISTED("Curriculum Subject Already existed"),
    STUDENT_CLASS_NOT_FOUND("Student class not found!"),
    STUDENT_NOT_FOUND("Student not found!"),
    STUDENT_CLASS_EXISTED("Student class existed!"),
    DELETE_STUDENT_CLASS_FAIL("Delete student class fail!"),
    ADD_STUDENT_CLASS_FAIL("Add student class fail!"),
    UPDATE_CAMERA_FAIL("Update camera fail!"),
    STUDENT_ALREADY_HAVE_CURRICULUM("Student already have curriculum"),
    STUDENT_DOES_NOT_HAVE_CURRICULUM("Student does not have curriculum"),
    SEMESTER_NOT_FOUND("Semester not found!"),
    SCHEDULE_NOT_FOUND("Schedule not found!"),
    TEACHER_NOT_FOUND("Teacher not found!"),
    ATTENDANCE_NOT_FOUND("Attendance not found!"),
    CAMERA_ALREADY_EXISTED("Camera already exists!"),
    CLASS_SUBJECT_EXISTED("Class subject existed!"),
    ADD_CLASS_SUBJECT_FAIL("Add class subject fail!"),
    CLASS_SUBJECT_NOT_FOUND("Class subject not found!"),
    TEACHER_SUBJECT_NOT_FOUND("Teacher subject not found!"),
    CHECK_ATTEND_INVALID_TIME("Data changes must be made before 00:00 today!"),
    SCHEDULE_FAILED_ROOM("The schedule has a different room!"),;

    private final String code;
}
