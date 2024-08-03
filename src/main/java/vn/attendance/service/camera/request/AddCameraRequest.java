package vn.attendance.service.camera.request;

import io.micrometer.core.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Required;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCameraRequest {

    private String ipTcpip;
    private String port;
    private String username;
    private String password;
    private String description;
    private Integer roomId;
    private String cameraType;
    private String checkType;
    private Integer deviceName; // nếu type LCD mới cho nhập
    @Nullable
    private String status;
    @Nullable
    private String errorMess;
}
