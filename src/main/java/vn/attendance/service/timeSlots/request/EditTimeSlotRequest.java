package vn.attendance.service.timeSlots.request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class EditTimeSlotRequest {
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;

    private String statusEdit;
    private String errorMess;
}
