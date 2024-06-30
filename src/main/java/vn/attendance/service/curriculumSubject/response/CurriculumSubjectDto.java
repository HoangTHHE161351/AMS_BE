package vn.attendance.service.curriculumSubject.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CurriculumSubjectDto {
    private Integer curriculumId;
    private Integer subjectId;
    private Integer semesterId;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
