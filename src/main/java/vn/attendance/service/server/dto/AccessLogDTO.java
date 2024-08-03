package vn.attendance.service.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AccessLogDTO {

    Integer id;

    Integer roomId;

    Integer userId;

    Integer similarity;

    Integer cameraId;

    String faceImage;

    String type;

    String fullName;

    String roleName;

    String cameraIp;

    String roomName;

    String checkType;

    LocalDateTime time;

    public AccessLogDTO(IAccessLogDto dto) {
        this.id = dto.getLogId();
        this.roomId = dto.getRoomId();
        this.userId = dto.getUserId();
        this.similarity = dto.getSimilarity();
        this.cameraId = dto.getCameraId();
        this.faceImage = dto.getFaceImage() != null ? new String(dto.getFaceImage()) : null;;
        this.type = dto.getType();
        this.fullName = dto.getFullName();
        this.roleName = dto.getRoleName();
        this.cameraIp = dto.getCameraIp();
        this.roomName = dto.getRoomName();
        this.checkType = dto.getCheckType();
        this.time = dto.getTimeStamp();
    }

}
