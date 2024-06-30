package vn.attendance.service.user.service.response;

import lombok.Data;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import vn.attendance.service.user.service.EmailService;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link vn.attendance.model.Users}
 */
@Data
public class UsersDto implements Serializable {
    Integer id;
    String roleName;
    //@Size(max = 100)
    String email;
    //@Size(max = 20)
    String username;
    //@Size(max = 50)
    String firstName;
    //@Size(max = 50)
    String lastName;
    String avata;
    String gender;
    //@Size(max = 200)
    String address;
    //@Size(max = 20)
    String phone;
    LocalDate dob;
    String description;
    //@Size(max = 20)
    String status;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime modifiedAt;
    String modifiedBy;

//    EmailService emailService = new EmailService();

    public UsersDto(Integer id, String roleName, String email, String username, String firstName
            , String lastName, String avata, String gender, String address, String phone, LocalDate dob, String description
            , String status, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        this.id = id;
        this.roleName = roleName;
//        this.email = emailService.maskEmail(email);
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avata = avata;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.dob = dob;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }
}