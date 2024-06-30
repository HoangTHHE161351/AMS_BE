package vn.attendance.service.server.response;

import lombok.Data;

@Data
public class DiskInfoDto {
    String number; //Số thứ tự ổ
    String partition; //Số phân vùng
    Integer capacity; // Dung lượng
    Integer free; // Dung lượng còn trống
    String Type; // Loại ổ
    String signal;
    String status; //Trạng thái ổ
}
