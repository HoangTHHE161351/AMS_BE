package vn.attendance.service.user.service.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ResetPasswordRequest {
    @NotEmpty(message = "Email must be not null")
    private String email;
    @NotEmpty(message = "OTP is require!")
    private String otp;
    @NotEmpty(message = "New Password is require")
    private String password;
}
