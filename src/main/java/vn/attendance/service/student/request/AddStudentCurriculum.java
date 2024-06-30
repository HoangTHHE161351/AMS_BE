package vn.attendance.service.student.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddStudentCurriculum {

    private Integer courseId;

    private Integer studentId;

    private String description;

    private String status;

    private String errorMess;
}
