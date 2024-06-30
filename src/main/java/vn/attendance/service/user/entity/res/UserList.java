package vn.attendance.service.user.entity.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import vn.attendance.model.Curriculum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserList {
    private Integer id;
    private String roleName;
    private String email;
    private String username;

    private String fullName;
    private Integer gender;
    private String address;
    private String phone;
    private LocalDate dob;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String curriculumName;



}
