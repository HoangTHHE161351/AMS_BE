package vn.attendance.service.schedule.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link vn.attendance.model.Schedule}
 */

@Data
@NoArgsConstructor
public class ScheduleDto {
    Integer id;
    String semester;
    String className;
    String subjectCode;
    Integer usersId;
    String teacherCode;
    LocalDateTime date;
    String time;
    Integer roomId;
    String room;
    String description;
    String status;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime modifiedAt;
    String modifiedBy;
    String attend;

    public ScheduleDto(Integer id, String semester, String className, String subjectCode, Integer usersId, String teacherCode,
                       LocalDateTime date, String time, Integer roomId, String room, String description, String status,
                       LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy, String attend) {
        this.id = id;
        this.semester = semester;
        this.className = className;
        this.subjectCode = subjectCode;
        this.usersId = usersId;
        this.teacherCode = teacherCode;
        this.date = date;
        this.time = time;
        this.roomId = roomId;
        this.room = room;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
        this.attend = attend;
    }

}
