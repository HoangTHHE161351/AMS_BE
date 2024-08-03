package vn.attendance.service.camera.request;

import io.micrometer.core.lang.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditCameraRequest {
    private Integer id;
    private String ipTcpip;
    private String port;
    private String username;
    private String password;
    private String description;
    private Integer roomId;
    private String cameraType;
    private String checkType;
    @Nullable
    private String status;
    @Nullable
    private String errorMess;
}
