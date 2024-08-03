package vn.attendance.service.classSubject.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddClassSubjectRequest {
    @NotNull(message = "classId is required")
    private Integer classId;

    @NotNull(message = "subjectId is required")
    private Integer subjectId;

    private String errorMess;
    private String status;
}