package vn.attendance.service.server.response;

import lombok.Data;

@Data
public class CameraInfoDto {
    String ip;
    Integer port;
    String username;
    String password;
    Integer channelId;
}
