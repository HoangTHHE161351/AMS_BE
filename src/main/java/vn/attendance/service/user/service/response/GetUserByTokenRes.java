package vn.attendance.service.user.service.response;

import lombok.Data;

@Data
public class GetUserByTokenRes {
    String fullName;
    String email;
}
