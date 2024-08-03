package vn.attendance.job.entity;

import lombok.Data;

@Data
public class ChangeRequest {
    Integer userId;
    Integer devId;
    Integer personType;
}
