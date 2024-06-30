package vn.attendance.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "daily_log", schema = "AMS")
public class DailyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Size(max = 255)
    @NotNull
    @Column(name = "activity_type", nullable = false)
    private String activityType;

    @Column(name = "activity_timestamp")
    private LocalDateTime activityTimestamp;

    @Lob
    @Column(name = "details")
    private String details;

}