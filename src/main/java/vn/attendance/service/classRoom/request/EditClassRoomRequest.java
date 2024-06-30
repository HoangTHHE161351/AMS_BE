package vn.attendance.service.classRoom.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Setter
public class EditClassRoomRequest {
    private String className;
    private String description;
    private String errorMess;
    private String status;

}