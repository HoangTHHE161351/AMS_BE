package vn.attendance.service.server.service.dto;


import java.time.LocalDateTime;

public interface IImageDto {
    Integer getId();
    byte[] getPic();
    LocalDateTime getTime();
}
