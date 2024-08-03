package vn.attendance.service.student.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StudentCurriculumDto {


     Integer id;
     String curriculumName;
     String userName;
     String studentName;
     String description;
     String status;
     LocalDateTime createdAt;
     String createdBy;
     LocalDateTime modifiedAt;
     String modifiedBy;



}
