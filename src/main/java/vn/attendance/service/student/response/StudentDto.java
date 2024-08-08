package vn.attendance.service.student.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StudentDto implements Serializable {
    Integer id;
    //@Size(max = 100)
    String email;
    //@Size(max = 20)
    String username;
    //@Size(max = 50)
    String fullname;
    //@Size(max = 50)
    String avata;
    Integer gender;
    //@Size(max = 200)
    String address;
    //@Size(max = 20)
    String phone;
    LocalDate dob;
    //@Size(max = 20)
    String status;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime modifiedAt;
    String modifiedBy;
//    String description;
    Integer curriculumId;
    String curriculumName;

    public StudentDto(Integer id, String email, String username, String fullname, String avata, Integer gender, String address, String phone, LocalDate dob, String status, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy , Integer curriculumId, String curriculumName) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.fullname = fullname;
        this.avata = avata;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.dob = dob;
        this.status = status;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
        this.curriculumId = curriculumId;
        this.curriculumName = curriculumName;
    }
}
