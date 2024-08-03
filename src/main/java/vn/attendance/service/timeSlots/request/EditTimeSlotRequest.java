package vn.attendance.service.timeSlots.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditTimeSlotRequest {
    private Integer id;
    private String startTime;
    private String endTime;
    private String slotName;
    private String description;
    private String statusEdit;
    private String errorMess;
}
