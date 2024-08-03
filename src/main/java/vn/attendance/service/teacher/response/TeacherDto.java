package vn.attendance.service.teacher.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TeacherDto {

    Integer id;
    Integer roleId;
    String email;
    String username;
    String name;
    String avata;
    Integer gender;
    String address;
    String phone;
    LocalDate dob;
    String status;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime modifiedAt;
    String modifiedBy;
    String description;
    String fieldName;

}

