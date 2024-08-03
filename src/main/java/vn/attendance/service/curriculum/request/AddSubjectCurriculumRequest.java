package vn.attendance.service.curriculum.request;

import io.micrometer.core.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddSubjectCurriculumRequest {

    String subjectCode;
    String curriculumName;
    Integer semesterNo;
    @Nullable
    private String status;
    @Nullable
    private String errorMess;

}
