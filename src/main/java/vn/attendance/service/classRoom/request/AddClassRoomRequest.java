package vn.attendance.service.classRoom.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddClassRoomRequest {
    @NotNull(message = "ClassRoom name is required")
    private String className;
    private String description;
    private String status;
    private String errorMess;
}
