package vn.attendance.service.user.service.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IUserDto {
    Integer getId();
    String getRoleName();
    String getEmail();
    String getUserName();
    String getFirstName();
    String getLastName();
    byte[] getAvata();
    String getGender();
    String getAddress();
    String getPhone();
    LocalDate getDob();
    String getDescription();
    String getStatus();
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    LocalDateTime getModifiedAt();
    String getModifiedBy();
}
