package vn.attendance.service.studentClass.response;

import lombok.Data;

@Data
public class StudentDto {
    Integer id;
    Integer studentId;
    String username;
    String fullName;
    String email;
    String image;

    public StudentDto(IStudentDto dto){
        this.id = dto.getId();
        this.studentId = dto.getStudentId();
        this.username = dto.getUsername();
        this.fullName = dto.getFullname();
        this.email = dto.getEmail();
        this.image = dto.getImage() != null ? new String(dto.getImage()) : null;
    }
}
