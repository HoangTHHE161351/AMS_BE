package vn.attendance.config.authen.entity;

import lombok.Data;
import vn.attendance.model.Users;

import java.time.Instant;

@Data
public class RefreshToken {
    private Users user;
    private String token;
    private Instant expiryDate;

    public RefreshToken() {
    }

    public RefreshToken(long id, Users user, String token, Instant expiryDate) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
