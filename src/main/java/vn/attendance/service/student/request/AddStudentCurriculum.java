package vn.attendance.service.student.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddStudentCurriculum {

    private Integer courseId;

    private Integer studentId;

    private String description;

    private String status;

    private String errorMess;
}
