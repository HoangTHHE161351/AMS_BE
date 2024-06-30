package vn.attendance.service.user.service.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ChangePasswordRequest {
    @NotEmpty(message = "OTP must be not null")
    private String otp;
    @NotEmpty(message = "Old Password must be not null")
    private String oldPassword;
    @NotEmpty(message = "New Password must be not null")
    private String newPassword;
}
