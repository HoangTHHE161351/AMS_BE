package vn.attendance.service.studentClass.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AddStudentClassRequest {
    private String username;

    private String className;

    private String errorMess;

    private String status;

}
