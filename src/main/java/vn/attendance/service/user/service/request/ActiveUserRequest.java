package vn.attendance.service.user.service.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ActiveUserRequest {
    @NotEmpty(message = "Email must be not null")
    private String email;
    @NotEmpty(message = "Password is require")
    private String password;
}
