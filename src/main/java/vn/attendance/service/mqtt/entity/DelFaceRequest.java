package vn.attendance.service.mqtt.entity;

import lombok.Data;

@Data
public class DelFaceRequest {
    private String customId;
    private Integer deviceId; // Device Id to add
}
