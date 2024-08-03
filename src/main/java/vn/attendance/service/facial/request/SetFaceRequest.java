package vn.attendance.service.facial.request;

import lombok.Data;

@Data
public class SetFaceRequest {
    Integer userId;
    Integer faceId;
}
