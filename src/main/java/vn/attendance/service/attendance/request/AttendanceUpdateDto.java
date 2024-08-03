package vn.attendance.service.attendance.request;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AttendanceUpdateDto {
    @NotNull
    private Integer attendanceId;
    @NotEmpty
    private String status;
}
