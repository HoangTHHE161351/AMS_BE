package vn.attendance.service.schedule.response;

import java.time.LocalDateTime;

public interface IScheduleDto {
    Integer getId();
    String getSemester();
    String getClassName();
    String getSubjectCode();
    Integer getUsersId();
    String getTeacherCode();
    LocalDateTime getDate();
    String getTime();
    String getRoom();
    String getDescription();
    String getStatus();
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    LocalDateTime getModifiedAt();
    String getModifiedBy();
}
