package vn.attendance.service.server.dto;

import java.time.LocalDateTime;

public interface IAccessLogDto {
    Integer getLogId();
    byte[] getFaceImage();
    LocalDateTime getTimeStamp();
    String getType();
    Integer getSimilarity();
    Integer getUserId();
    String getFullName();
    String getRoleName();
    Integer getCameraId();
    String getCameraIp();
    String getCheckType();
    Integer getRoomId();
    String getRoomName();
}
