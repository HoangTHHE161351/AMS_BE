package vn.attendance.service.user.entity.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
    @NotEmpty(message = "Username must be not null or empty")
    private String username;
    @NotEmpty(message = "Password must be not null or empty")
    private String password;
}
