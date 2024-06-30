package vn.attendance.service.curriculumSubject.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditCurriculumSubjectRequest {
    private Integer curriculum_id;
    private Integer subject_id;
    private Integer semester_id;
    private String status;
    private String errorMess;
}
