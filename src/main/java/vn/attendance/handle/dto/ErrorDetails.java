package vn.attendance.handle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorDetails {
    private Date timestamp;

    private String message;

    private Object details;

    private String path;
}
