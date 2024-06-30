package vn.attendance.service.user.service.request;

import lombok.Data;
import vn.attendance.validate.StatusTypeConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ChangeStatusRequest {

    @NotNull(message = "Id is required")
    int id;
    @NotEmpty(message = "Status is required")
    @StatusTypeConstraint(message = "Status is invalid")
    String status;
}
