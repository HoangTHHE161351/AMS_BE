package vn.attendance.service.subject.response;

import lombok.Data;

import java.time.LocalDateTime;

public interface ISubjectDto {

    Integer getId();
    String getName();
    String getCode();
}
