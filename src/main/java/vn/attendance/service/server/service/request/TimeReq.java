package vn.attendance.service.server.service.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimeReq {
    LocalDate date;
    LocalTime time;
}
