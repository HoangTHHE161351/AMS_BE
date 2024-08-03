package vn.attendance.service.attendance.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class AttendanceDto {

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
    @JsonIgnore
    LocalDateTime createdAt;
    String createdBy;
    @JsonIgnore
    LocalDateTime modifiedAt;
    String modifiedBy;

    @JsonIgnore
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public AttendanceDto(IAttendanceDto dto) {
        this.id = dto.getId();
        this.userId = dto.getUserId();
        this.room = dto.getRoomName();
        this.day = dto.getDay();
        this.learnDate = dto.getLearnDate();
        this.userName = dto.getName();
        this.teacherCode = dto.getTeacherCode();
        this.className = dto.getClassName();
        this.subjectCode = dto.getSubjectCode();
        this.slot = dto.getSlot();
        this.description = dto.getDescription();
        this.status = dto.getStatus();
        this.createdAt =  dto.getCreatedAt();
        this.createdBy = dto.getCreatedBy();
        this.modifiedAt = dto.getModifiedAt();
        this.modifiedBy = dto.getModifiedBy();
    }

}
