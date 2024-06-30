package vn.attendance.service.attendance.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AutoAttendance {

//    String room;
    String UserName;
    LocalDateTime time;

}
