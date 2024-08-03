package vn.attendance.service.server.dto;

import java.time.LocalDateTime;

public interface ILogDto {
    Integer getId();
    LocalDateTime getTime();
    Integer getCameraId();
    String getCameraType();
    String getCheckType();
}
