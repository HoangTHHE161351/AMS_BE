package vn.attendance.service.timeSlots.response;


import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
public class TimeSlotDto {
    private Integer id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String slotName;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public TimeSlotDto(Integer id, LocalTime startTime, LocalTime endTime, String slotName, String description, String status, LocalDateTime createdAt,
                       String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotName = slotName;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }
}
