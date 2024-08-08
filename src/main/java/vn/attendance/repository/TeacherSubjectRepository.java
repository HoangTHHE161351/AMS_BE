package vn.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.attendance.model.TeacherSubject;
import vn.attendance.service.teacherSubject.response.TeacherSubjectDto;

import java.util.List;

public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Integer> {

    @Query("select t from TeacherSubject t " +
            "where t.teacherId = :teacherId and t.subjectId = :subId")
    TeacherSubject findByTeacherAndSubId(Integer teacherId, Integer subId);

    @Query(value = "select ts.* from teacher_subject ts where ts.subject_id = :subjectId and ts.status = 'ACTIVE' ",nativeQuery = true)
    List<TeacherSubject> findBySubject(Integer subjectId);

    @Query(value = "select ts.* from teacher_subject ts where ts.teacher_id = :teacherId and ts.status = 'ACTIVE' ",nativeQuery = true)
    List<TeacherSubject> findByTeacher(Integer teacherId);

    @Query("SELECT new vn.attendance.service.teacherSubject.response.TeacherSubjectDto(ts.id, s.code," +
            "s.name, ts.status, ts.createdAt, uc.username, ts.modifiedAt, um.username) " +
            "FROM TeacherSubject ts " +
            "join Users u on ts.teacherId = u.id and u.status = 'ACTIVE' " +
            "join Subject s on ts.subjectId = s.id and s.status = 'ACTIVE' " +
            "LEFT JOIN Users uc ON ts.createdBy = uc.id " +
            "LEFT JOIN Users um ON ts.modifiedBy = um.id " +
            "WHERE (:id is null or ts.teacherId = :id)")
    List<TeacherSubjectDto> findSubjectsByTeacher(Integer id);
}