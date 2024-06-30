package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.attendance.model.Subject;
import vn.attendance.model.Users;
import vn.attendance.service.subject.response.SubjectDto;

import java.util.List;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query("SELECT c from  Subject c")
    Page<Subject> findAllSubject(Pageable pageable);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    @Query("select s from Subject s where s.status = 'ACTIVE'")
    List<Subject> findAllSubject();

    @Query("select s from Subject s")
    List<Subject> findAllSubjects();

    @Query("SELECT new vn.attendance.service.subject.response.SubjectDto(t.id, t.curriculumId, t.name, t.code, " +
            "t.status, t.createdAt, uc.username, t.modifiedAt, um.username, t.slots) " +
            "FROM Subject t " +
            "LEFT JOIN Users uc ON t.createdBy = uc.id " +
            "LEFT JOIN Users um ON t.modifiedBy = um.id " +
            "JOIN Curriculum r ON t.curriculumId = r.id " +
            "WHERE (:search IS NULL OR t.name LIKE CONCAT('%', :search, '%'))")
    Page<SubjectDto> searchSubject(@Param("search") String search, Pageable pageable);
}