package vn.attendance.service.user.entity.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface  UserDto {
    Integer getId();
    String getUsername();
    String getFirstname();
    String getLastname();
    String getEmail();
    String getAvatar();
    Integer getGender();
    String getAddress();
    String getPhone();
    LocalDate getDob();
    String getRole();
    String getStatus();

    String getCreateby();
    LocalDateTime getCreateat();
    String getModifiedby();
    LocalDateTime getModifiedat();

}
