package vn.attendance.service.accessLog.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AccessLogInRoomDto implements Serializable {
    Integer id;

    Integer roomId;

    String roomName;

    String userName;

    String name;

    LocalTime checkIn;

    LocalTime checkOut;

    LocalDate dateTime;

    public AccessLogInRoomDto(IAccessLogInRoomDto dto) {
        this.id = dto.getId();
        this.roomName = dto.getRoomName();
        this.roomId = dto.getRoomId();
        this.userName = dto.getUserName();
        this.name = dto.getName();
        this.checkIn = dto.getCheckIn();
        this.checkOut = dto.getCheckOut();
        this.dateTime = dto.getDateTime();
    }
}
