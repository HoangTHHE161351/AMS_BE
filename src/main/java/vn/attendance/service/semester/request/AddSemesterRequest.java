package vn.attendance.service.semester.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSemesterRequest {
    private String semesterName;
    private LocalDate startTime;
    private LocalDate endTime;
    private String description;
    private String status;
    private String errorMess;


}
