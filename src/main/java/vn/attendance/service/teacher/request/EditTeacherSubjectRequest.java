package vn.attendance.service.teacher.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class EditTeacherSubjectRequest {

    private Integer teacherId;
    private Integer subjectId;
    private String status;

}
