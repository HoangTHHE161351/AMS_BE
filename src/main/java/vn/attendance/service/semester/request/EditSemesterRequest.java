package vn.attendance.service.semester.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditSemesterRequest {
    private Integer id;
    private String semesterName;
    private LocalDate startTime;
    private LocalDate endTime;
    private String description;
    private String status;
    private String errorMess;

}
