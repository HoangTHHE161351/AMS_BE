package vn.attendance.service.subject.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EditSubjectRequest {

    private Integer id;
    private Integer curriculumId;
    private String name;
    private String code;
    private Integer slots;

}
