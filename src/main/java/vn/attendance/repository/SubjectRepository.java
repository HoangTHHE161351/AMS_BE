package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.attendance.model.Subject;
import vn.attendance.model.Users;
import vn.attendance.service.subject.response.ISubjectDto;
import vn.attendance.service.subject.response.SubjectDto;

import java.time.LocalDate;
import java.util.List;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    boolean existsByCode(String code);

    boolean existsByName(String name);

//    @Query("select s from Subject s where s.status = 'ACTIVE'")
//    List<Subject> findAllSubject();

    @Query("select s from Subject s where s.status = 'ACTIVE'")
    List<Subject> findAllSubjects();

    @Query(value = "select s.id as id, s.code as code, s.name as name from subjects s where s.status = 'ACTIVE'", nativeQuery = true)
    List<ISubjectDto> getDropdownSubject();

    @Query("SELECT new vn.attendance.service.subject.response.SubjectDto(t.id, t.name, t.code, " +
            "t.status, t.createdAt, uc.username, t.modifiedAt, um.username, t.slots) " +
            "FROM Subject t " +
            "LEFT JOIN Users uc ON t.createdBy = uc.id " +
            "LEFT JOIN Users um ON t.modifiedBy = um.id " +
            "WHERE :search IS NULL OR t.name LIKE CONCAT('%', :search, '%') OR t.code LIKE CONCAT('%', :search, '%')")
    Page<SubjectDto> searchSubject(@Param("search") String search, Pageable pageable);

    @Query(value = "select * from subjects s where s.code = :subjectCode and s.status = 'ACTIVE' ", nativeQuery = true)
    Subject findSubjectByCode(String subjectCode);

    @Query(value = "select * from subjects s where s.name = :name and s.status = 'ACTIVE' ", nativeQuery = true)
    Subject findSubjectByName(String name);

    @Query(value = "select count(*) from schedules s where s.subject_id = :subjectId and Date(s.learn_timestamp) >= Date(:now) and s.status = 'ACTIVE' ",nativeQuery = true)
    Integer countScheduleBySubject(Integer subjectId, LocalDate now);

}