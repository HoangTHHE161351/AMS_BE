package vn.attendance.service.attendance.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddAttendanceRequest {

    private Integer userId;

    private Integer scheduleId;

    private String description;

    private String status;

    private String errorMess;
}
