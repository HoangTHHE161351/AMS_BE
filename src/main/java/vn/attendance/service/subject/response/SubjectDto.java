package vn.attendance.service.subject.response;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class SubjectDto {


    Integer id;

    String name;

    String code;

    String status;

    LocalDateTime createdAt;

    String createdBy;

    LocalDateTime modifiedAt;

    String modifiedBy;

    Integer slots;


    public SubjectDto(Integer id,
                      String name,
                      String code,
                      String status,
                      LocalDateTime createdAt,
                      String createdBy,
                      LocalDateTime modifiedAt,
                      String modifiedBy,
                      Integer slots) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.status = status;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
        this.slots = slots;


    }
}
