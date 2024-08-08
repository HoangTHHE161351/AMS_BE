package vn.attendance.service.room.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class EditRoomRequest {
    private Integer id;
    private String roomName;
    private String description;
    private String errorMess;
    private String status;
}
