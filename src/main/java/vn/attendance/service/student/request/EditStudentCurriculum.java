package vn.attendance.service.student.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditStudentCurriculum {
    private Integer courseId;

    private String description;

    private String status;

    private String errorMess;
}
