package vn.attendance.service.accessLog.response;

import java.time.LocalDateTime;
import java.time.LocalTime;


public interface IAccessLogDTO {
    Integer getId();

    byte[] getFaceImage();

    String getRoomName();

    String getUserName();

    String getDay();

    LocalTime getCheckTime();

    String getCheckType();
}
