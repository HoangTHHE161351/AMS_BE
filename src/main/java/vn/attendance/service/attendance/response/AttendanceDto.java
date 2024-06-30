package vn.attendance.service.attendance.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceDto implements Serializable {

   Integer id;

   Integer userId;

   String room;

   String day;

   LocalDate learnDate;

   String userName;

   String teacherCode;

   String className;

   String subjectCode;

   String slot;

   String description;

   String status;

   LocalDateTime createdAt;

   String createdBy;

   LocalDateTime modifiedAt;

   String modifiedBy;

    public AttendanceDto(Integer id, Integer userId, String room, String day, LocalDate learnDate, String userName,
                         String teacherCode, String className, String subjectCode, String slot, String description,
                         String status, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        this.id = id;
        this.userId = userId;
        this.room = room;
        this.day = day;
        this.learnDate = learnDate;
        this.userName = userName;
        this.teacherCode = teacherCode;
        this.className = className;
        this.subjectCode = subjectCode;
        this.slot = slot;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }
}
