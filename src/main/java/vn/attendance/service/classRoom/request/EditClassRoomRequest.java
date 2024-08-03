package vn.attendance.service.classRoom.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditClassRoomRequest {
    private String className;
    private String description;
    private String errorMess;
    private String status;
}