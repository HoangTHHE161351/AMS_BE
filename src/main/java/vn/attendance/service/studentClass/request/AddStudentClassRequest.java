package vn.attendance.service.studentClass.request;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddStudentClassRequest {
    private String username;

    private String className;

    private String errorMess;

    private String status;

}
