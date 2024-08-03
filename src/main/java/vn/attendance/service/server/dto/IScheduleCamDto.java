package vn.attendance.service.server.dto;

import java.time.LocalDateTime;

public interface IScheduleCamDto {
    Integer getId();
    Integer getTimeSlot();
    Integer getCameraId();
}
