package vn.attendance.service.camera.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCameraRequest {
    private String ipTcpip;
    private String port;
    private String description;
    private Integer id;
}
