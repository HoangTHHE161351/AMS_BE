package vn.attendance.service.user.entity.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginReq {
    @NotNull(message = "Username  must be not empty")
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String dob;
    private Long sex;
    @NotNull(message = "Email  must be not empty")
    private String email;
    private String phoneNumber;
    private String address;
    private String avatar;
    @NotEmpty(message = "Role must be not empty!")
    private String roleName;
}
