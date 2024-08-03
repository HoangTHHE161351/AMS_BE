package vn.attendance.service.facial.request;

import lombok.Data;

@Data
public class AddFacialRequest {

    String image;

    String status;

    String errorMess;

}
