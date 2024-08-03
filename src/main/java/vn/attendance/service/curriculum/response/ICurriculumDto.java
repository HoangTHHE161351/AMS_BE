package vn.attendance.service.curriculum.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public interface ICurriculumDto {

    Integer getId();

    String getCurriculumName();

    String getDescription();

    String getStatus();

}
