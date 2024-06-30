package vn.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.attendance.model.StudentCurriculum;

public interface StudentCurriculumRepository extends JpaRepository<StudentCurriculum, Integer> {
}