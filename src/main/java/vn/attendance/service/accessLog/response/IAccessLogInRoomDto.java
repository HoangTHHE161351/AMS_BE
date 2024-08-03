package vn.attendance.service.accessLog.response;

import java.time.LocalDate;
import java.time.LocalTime;

public interface IAccessLogInRoomDto {

    Integer getId();

    String getRoomName();

    Integer getRoomId();

    String getUserName();

    String getName();

    LocalTime getCheckIn();

    LocalTime getCheckOut();

    LocalDate getDateTime();


}
