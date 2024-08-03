package vn.attendance.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "curriculum_sub", schema = "AMS")
public class CurriculumSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "curriculum_id")
    private Integer curriculumId;

    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "semester_id")
    private Integer semesterId;

    @Column(name ="status")
    private String status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "modified_by")
    private Integer modifiedBy;
}
