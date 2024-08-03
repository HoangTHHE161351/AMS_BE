package vn.attendance.service.teacherSubject.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditTeacherSubjectRequest {
    private Integer curriculum_id;
    private Integer subject_id;
    private Integer semester_id;
    private String status;
    private String errorMess;
}
