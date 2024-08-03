package vn.attendance.service.teacherSubject.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TeacherSubjectDto {
    private Integer id;
    private String subjectCode;
    private String subjectName;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
