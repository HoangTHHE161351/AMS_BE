package vn.attendance.service.schedule.request;

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
public class AddScheduleRequest {
    private String semester;
    private String className;
    private String subjectCode;
    private String teacherCode;
    private LocalDate date;
    private String timeSlot;
    private String room;
    private String description;
    private String status;
    private String errorMess;
}
