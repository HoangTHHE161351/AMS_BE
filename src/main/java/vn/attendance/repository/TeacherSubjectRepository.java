package vn.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.attendance.model.TeacherSubject;

public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Integer> {

    @Query("select t from TeacherSubject t " +
            "where t.teacherId = :teacherId and t.subjectId = :subId and t.status = 'ACTIVE'")
    TeacherSubject findByTeacherAndSubId(Integer teacherId, Integer subId);
}