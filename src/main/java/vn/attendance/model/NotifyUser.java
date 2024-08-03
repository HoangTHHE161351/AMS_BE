package vn.attendance.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notify_user", schema = "AMS")
public class NotifyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "notify_id")
    private Integer notifyId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "is_read")
    private Integer isRead;
}