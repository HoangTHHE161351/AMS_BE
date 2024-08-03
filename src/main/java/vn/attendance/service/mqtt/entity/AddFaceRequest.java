package vn.attendance.service.mqtt.entity;

import lombok.Data;

@Data
public class AddFaceRequest {
    private String customId;
    private String name;
    private Integer gender; // 0: Male, 1: Female
    private Integer tempCardType; // 0: Permanent, 1: Temporary
    private Integer personType; // 0: White list, 1: Black list
    private Integer cardType; // 0: ID card
    private Integer deviceId; // Device Id to add
    private String pic; // Base64 encoded image
}
