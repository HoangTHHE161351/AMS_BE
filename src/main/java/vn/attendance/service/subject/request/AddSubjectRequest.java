package vn.attendance.service.subject.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSubjectRequest {
    private String code;
    private String name;
    private Integer slots;
    private String status;
    private String errorMess;

}
