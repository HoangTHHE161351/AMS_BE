package vn.attendance.service.studentClass.response;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@RequiredArgsConstructor
public class StudentClassDto {

    private Integer id;

    private String studentName;

    private String className;

    private String status;

    private String createdBy;

    private LocalDateTime createdAt;

    private String modifiedBy;

    private LocalDateTime modifiedAt;

    public StudentClassDto(Integer id, String studentName, String className, String status, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        this.id = id;
        this.studentName = studentName;
        this.className = className;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.modifiedBy = modifiedBy;
        this.modifiedAt = modifiedAt;
    }
}