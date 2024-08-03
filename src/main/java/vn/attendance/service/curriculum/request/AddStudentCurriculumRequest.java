package vn.attendance.service.curriculum.request;

import io.micrometer.core.lang.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddStudentCurriculumRequest {
    String username;
    String curriculumName;
    @Nullable
    private String status;
    @Nullable
    private String errorMess;
}
