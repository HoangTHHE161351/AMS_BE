package vn.attendance.service.student.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditStudentCurriculum {
    private Integer courseId;

    private String description;

    private String status;

    private String errorMess;
}
