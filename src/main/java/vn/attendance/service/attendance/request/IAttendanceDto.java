package vn.attendance.service.attendance.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IAttendanceDto {
    Integer getId();
    Integer getUserId();
    String getDay();
    LocalDate getLearnDate();
    String getName();
    String getTeacherCode();
    String getClassName();
    String getRoomName();
    String getSubjectCode();
    String getSlot();
    String getDescription();
    String getStatus();
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    LocalDateTime getModifiedAt();
    String getModifiedBy();
}
