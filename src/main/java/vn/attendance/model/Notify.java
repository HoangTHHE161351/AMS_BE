package vn.attendance.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notify", schema = "AMS")
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "title", length = 100)
    private String title;

    @Size(max = 200)
    @Column(name = "content", length = 200)
    private String content;

    @Column(name = "destination_page")
    private String destinationPage;

    @Column(name = "page_params")
    private String pageParams;

    @Column(name = "time")
    private LocalDateTime time;
}