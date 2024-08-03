package vn.attendance.service.studentClass.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class EditStudentClassRequest {

    private Integer studentId;

    private Integer classId;

    private String errorMess;

    private String status;

}
