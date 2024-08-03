package vn.attendance.service.accessLog.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import vn.attendance.util.DataUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class AccessLogDTO {

    Integer id;

    String faceImage;

    String roomName;

    String userName;

    String day;

    LocalTime time;

    String check;

    public AccessLogDTO(IAccessLogDTO dto) {
        this.id = dto.getId();
        this.faceImage = dto.getFaceImage() != null ? new String(dto.getFaceImage()) : null;
        this.roomName = dto.getRoomName();
        this.userName = dto.getUserName();
        this.day = dto.getDay();
        this.time = dto.getCheckTime();
        this.check = dto.getCheckType();
    }
}
