package vn.attendance.service.camera.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraRes {
    Integer id;
    String name;
    String ip;
    Integer port;
    String description;
    String status;
    Integer roomId;
    String roomName;
    String cameraType;
    String checkType;

    public CameraRes(ICameraRes iCameraRes){
        this.id = iCameraRes.getId();
        this.ip = iCameraRes.getIp();
        this.port = iCameraRes.getPort();
        this.description = iCameraRes.getDescription();
        this.status = iCameraRes.getStatus();
        this.roomName = iCameraRes.getRoomName();
        this.cameraType = iCameraRes.getCameraType();
        this.checkType = iCameraRes.getCheckType();
        this.roomId = iCameraRes.getRoomId();
        this.name = iCameraRes.getName();
    }
}
