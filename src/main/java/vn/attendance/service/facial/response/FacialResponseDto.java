package vn.attendance.service.facial.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FacialResponseDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String image;

    private String status;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime modifiedAt;

    private String modifiedBy;
}
