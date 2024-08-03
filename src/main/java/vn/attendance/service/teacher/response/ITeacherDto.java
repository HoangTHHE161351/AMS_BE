package vn.attendance.service.teacher.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ITeacherDto {
    Integer getId();
    String getName();
    String getUsername();
}

