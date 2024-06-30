package vn.attendance.service.cameraRoom.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCameraRoomRequest {
    private Integer cameraId;
    private Integer roomId;
    private String location;
    private String status;
    private String errorMess;
}
