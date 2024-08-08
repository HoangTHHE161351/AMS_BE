package vn.attendance.service.attendance.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditAttendanceRequest {

    private Integer id;

    private Integer description;

    private String status;

    private String errorMess;
}
