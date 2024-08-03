package vn.attendance.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.swing.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "AMS")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password")
    private String password;

    @Size(max = 20)
    @Column(name = "user_name", length = 20)
    private String username;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Lob
    @Column(name = "avata")
    private String avata;

    @Column(name = "gender")
    private Integer gender;

    @Size(max = 200)
    @Column(name = "address", length = 200)
    private String address;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "dob")
    private LocalDate dob;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "token_expire")
    private LocalDateTime tokenExpire;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_expire")
    private LocalDateTime otpExpire;

    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 20)
    @Column(name = "face_id")
    private  String faceId;
}