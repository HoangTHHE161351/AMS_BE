package vn.attendance.service.mqtt.entity;

import lombok.Data;

@Data
public class ChangeStateRequest {
    private String customId;
    private Integer personType; // 0: White list, 1: Black list
    private Integer deviceId; // Device Id to add
}
