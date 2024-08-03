package vn.attendance.service.studentClass.response;

public interface IStudentDto {

    Integer getId();

    Integer getStudentId();

    String getUsername();

    String getFullname();

    String getEmail();

    byte[] getImage();
}
