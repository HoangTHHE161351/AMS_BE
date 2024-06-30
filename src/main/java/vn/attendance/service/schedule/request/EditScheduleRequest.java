package vn.attendance.service.schedule.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class EditScheduleRequest {
    private Integer id;
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
