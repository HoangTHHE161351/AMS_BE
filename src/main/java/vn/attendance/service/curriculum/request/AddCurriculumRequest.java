package vn.attendance.service.curriculum.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class AddCurriculumRequest {

    @NotEmpty(message = "Curriculum Name is required")
    private String curriculumName;

    @NotEmpty(message = "Description is required")
    private String description;

}
