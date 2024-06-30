package vn.attendance.service.room.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AddRoomRequest {

    @NotNull(message = "Room name is required")
    private String roomName;
    private String description;
    private String status;
    private String errorMess;
    private LocalDateTime createdAt;
    private Integer createdBy;

}
