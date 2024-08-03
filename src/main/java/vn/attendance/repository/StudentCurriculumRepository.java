package vn.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.attendance.model.StudentCurriculum;

public interface StudentCurriculumRepository extends JpaRepository<StudentCurriculum, Integer> {

    @Query(value = "select * from student_curriculum sc where sc.status = 'ACTIVE' and sc.student_id = :studentId ", nativeQuery = true)
    StudentCurriculum findByStudentId(Integer studentId);

    @Query(value = "select count(*) from student_curriculum sc where sc.curriculum_id = :id and sc.status='ACTIVE' ", nativeQuery = true)
    Integer getCountStudentCurriculum(Integer id);
}