package vn.attendance.service.curriculum.request;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditCurriculumRequest {

    @NotEmpty(message = "Id is required")
    private Integer id;

    @NotEmpty(message = "Curriculum Name is required")
    private String curriculumName;

    @NotEmpty(message = "Description is required")
    private String description;

    private String status;
}
