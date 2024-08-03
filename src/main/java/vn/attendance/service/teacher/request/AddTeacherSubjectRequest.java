package vn.attendance.service.teacher.request;

import lombok.Data;

@Data
public class AddTeacherSubjectRequest {
    private String teacherCode;
    private String subjectCode;
    private String status;
    private String errorMess;
}
