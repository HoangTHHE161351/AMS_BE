package vn.attendance.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "cameras", schema = "AMS")
public class Camera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "ip_tcip", length = 50)
    private String ipTcip;

    @Size(max = 30)
    @Column(name = "port", length = 30)
    private String port;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    @Size(max = 20)
    @Column(name = "username", length = 20)
    private String username;

    @Size(max = 32)
    @Column(name = "password", length = 20)
    private String password;

    @Size(max = 20)
    @Column(name = "camera_type", length = 20)
    private String cameraType;

    @Size(max = 20)
    @Column(name = "check_type", length = 20)
    private String checkType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "channel_id")
    private Integer channelId;

    @Column(name = "room_id")
    private Integer roomId;
}