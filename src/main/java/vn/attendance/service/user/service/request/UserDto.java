package vn.attendance.service.user.service.request;

import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import vn.attendance.service.user.service.EmailService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserDto {
    Integer id;
    String roleName;
    String email;
    String username;
    String firstName;
    String lastName;
    String avata;
    String gender;
    String address;
    String phone;
    LocalDate dob;
    String description;
    String status;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime modifiedAt;
    String modifiedBy;

//    EmailService emailService = new EmailService();

    public UserDto(IUserDto dto) {
        this.id = dto.getId();
        this.roleName = dto.getRoleName();
//        this.email = emailService.maskEmail(dto.getEmail());
        this.email = dto.getEmail();
        this.username = dto.getUserName();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.avata = dto.getAvata() != null ? new String(dto.getAvata()) : null;
        this.gender = dto.getGender();
        this.address = dto.getAddress();
        this.phone = dto.getPhone();
        this.dob = dto.getDob();
        this.description = dto.getDescription();
        this.status = dto.getStatus();
        this.createdAt = dto.getCreatedAt();
        this.createdBy = dto.getCreatedBy();
        this.modifiedAt = dto.getModifiedAt();
        this.modifiedBy = dto.getModifiedBy();
    }
}
