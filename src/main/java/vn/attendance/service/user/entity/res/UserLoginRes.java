package vn.attendance.service.user.entity.res;

import lombok.Data;
import vn.attendance.model.Users;

import java.util.List;

@Data
public class UserLoginRes {
    private String username;
    private String token;
    private String refreshToken;
    private List<String> roles;
    private Users user;
}
