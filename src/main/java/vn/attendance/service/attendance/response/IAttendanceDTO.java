package vn.attendance.service.attendance.response;

import java.time.LocalDateTime;

public interface IAttendanceDTO {

   Integer getId();

   String getUserName();

   String getName();

   String getClassName();

   String getSubjectCode();

   String getSlot();

   String getDescription();

   String getStatus();

   LocalDateTime getCreatedAt();

   String getCreatedBy();

   LocalDateTime getModifiedAt();

   String getModifiedBy();

   byte[] getAvatar();
}
