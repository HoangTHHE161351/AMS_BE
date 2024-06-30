package vn.attendance.service.semester.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.attendance.service.semester.request.ISemesterDto;
import vn.attendance.service.user.service.request.IUserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
//@NoArgsConstructor
public class SemesterDto {
    Integer id;
     String semesterName;
     LocalDateTime startTime;
     LocalDateTime endTime;
     String description;
     String status;
     LocalDateTime createdAt;
     String createdBy;
     LocalDateTime modifiedAt;
    String modifiedBy;

    // Constructor
    public SemesterDto(Integer id, String semesterName, LocalDateTime startTime,
                       LocalDateTime endTime, String description, String status, LocalDateTime createdAt,
                       String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        this.id = id;
        this.semesterName = semesterName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }


}

