package vn.attendance.service.server.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SendImageRequest {
    private String picture;
    private LocalDateTime time;
    private Integer cameraId;
}
