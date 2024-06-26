package vn.attendance.service.student.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudentDto implements Serializable {
    Integer id;
    //@Size(max = 100)
    String email;
    //@Size(max = 20)
    String username;
    //@Size(max = 50)
    String fullname;
    //@Size(max = 50)
    String curriculum;
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

    public StudentDto(Integer id, String email, String username, String fullname, String curriculum, String avata, Integer gender, String address, String phone, LocalDate dob, String status, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.fullname = fullname;
        this.curriculum = curriculum;
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
//        this.description = description;
    }
}
