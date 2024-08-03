package vn.attendance.service.room.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Setter
public class EditRoomRequest {
    private Integer id;
    private String roomName;
    private String description;
    private String errorMess;
    private String status;
}
