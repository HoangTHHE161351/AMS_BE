package vn.attendance.service.attendance.response;

import lombok.Data;

@Data
public class ListAttendenceResponse {
    private Integer attendenceId;
    private String firstName;
    private String lastName;
    private String roleName;
    private String email;
    private String phoneNum;
    private Integer gender;
    private String dob;
    private String image;
    private String status;

    public ListAttendenceResponse(IListAttendenceResponse data) {
        this.attendenceId = data.getAttendenceId();
        this.firstName = data.getFirstName();
        this.lastName = data.getLastName();
        this.roleName = data.getRoleName();
        this.email = data.getEmail();
        this.phoneNum = data.getPhoneNum();
        this.gender = data.getGender();
        this.dob = data.getDob();
        this.image = data.getImage()!= null ? new String(data.getImage()) : null;
        this.status = data.getStatus();
    }
}