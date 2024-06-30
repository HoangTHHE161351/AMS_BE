package vn.attendance.service.timeSlots.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class TimeSlotDto {
    private Integer id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private String status;
//    private LocalDateTime createdAt;
//    private String createdBy;
//    private LocalDateTime modifiedAt;
//    private String modifiedBy;

}
