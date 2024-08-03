package vn.attendance.service.room.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AddRoomRequest {
    @Size(max = 50)
    @NotNull(message = "Room name is required")
    private String roomName;
    @Size(max = 255)
    private String description;
    @Size(max = 20)
    private String status;
    private String errorMess;
}
