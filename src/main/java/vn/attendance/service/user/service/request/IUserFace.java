package vn.attendance.service.user.service.request;

import java.time.LocalDate;

public interface IUserFace {
    Integer getId();
    String getUsername();
    LocalDate getDob();
    String getStatus();
    String getFullname();
    Integer getGender();
    String getAddress();
    String getImage();
}
