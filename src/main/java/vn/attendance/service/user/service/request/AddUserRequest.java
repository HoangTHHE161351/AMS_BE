package vn.attendance.service.user.service.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import vn.attendance.validate.SoDienThoaiConstraint;

import javax.validation.Constraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AddUserRequest {
    private String avatar;
    @NotNull(message = "Username is required")
    private String username;
    private String firstName;
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String dob;
    private Integer sex;
    @NotNull(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
    @SoDienThoaiConstraint(message = "Phone is invalid")
    private String phoneNumber;
    private String address;
    @NotEmpty(message = "Role must be not empty!")
    private String roleName;
    private String status;
    private String errorMess;
}
