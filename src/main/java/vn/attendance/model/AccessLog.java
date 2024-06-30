package vn.attendance.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "access_log", schema = "AMS")
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "face_image")
    private String faceImage;

    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    @Column(name = "type")
    private Integer type;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "similarity")
    private Integer similarity;

}