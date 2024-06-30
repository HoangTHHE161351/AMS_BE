package vn.attendance.service.semester.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ISemesterDto {
    Integer getId();
    String getsemesterName();
    LocalDateTime getStarttime();
    LocalDateTime getEndtime();
    String getDescription();
    String getStatus();
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    LocalDateTime getModifiedAt();
    String getModifiedBy();
}
