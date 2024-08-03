package vn.attendance.service.user.entity.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenRefreshReq {
    @NotBlank
    private String refreshToken;
}
