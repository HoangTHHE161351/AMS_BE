package vn.attendance.service.server.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FaceDetectRes {
    String picture;
    Integer logId;
}
