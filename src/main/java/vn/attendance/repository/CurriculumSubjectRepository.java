package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.attendance.model.CurriculumSubject;
import vn.attendance.service.curriculumSubject.response.CurriculumSubjectDto;

import java.util.List;

public interface CurriculumSubjectRepository  extends JpaRepository<CurriculumSubject, Integer> {
    @Query("SELECT cs FROM CurriculumSubject cs " +
            "order by cs.createdAt desc")
    Page<CurriculumSubject> findAllCurriculumSub(Pageable pageable);

    @Query(value = "SELECT * FROM curriculum_sub cs WHERE cs.curriculum_id = :id and cs.status = 'ACTIVE' ", nativeQuery = true)
    List<CurriculumSubject> findAllByCurriculum(Integer id);

    @Query("SELECT new vn.attendance.service.curriculumSubject.response.CurriculumSubjectDto(c.id,cs.curriculumName, s.code," +
            "s.name,sm.semesterName, " +
            "c.status, c.createdAt, uc.username, c.modifiedAt, um.username) " +
            "FROM CurriculumSubject c " +
            "join Curriculum cs on c.curriculumId = c.id and c.status = 'ACTIVE' " +
            "join Subject s on c.subjectId = s.id and s.status = 'ACTIVE' " +
            "join Semester sm on c.semesterId = sm.id and sm.status = 'ACTIVE' " +
            "LEFT JOIN Users uc ON c.createdBy = uc.id " +
            "LEFT JOIN Users um ON c.modifiedBy = um.id " +
            "WHERE (:id is null or c.curriculumId = :id) order by cs.createdAt desc")
    List<CurriculumSubjectDto> findAllByCurriculumId(Integer id);

    CurriculumSubject findByCurriculumIdAndSubjectIdAndSemesterId(Integer curriculumId, Integer subjectId, Integer semesterId);



    @Query("SELECT new vn.attendance.service.curriculumSubject.response.CurriculumSubjectDto(" +
            " c.id,cs.curriculumName, s.code, s.name, sm.semesterName, " +
            " c.status, c.createdAt, uc.username, c.modifiedAt, um.username) " +
            "FROM CurriculumSubject c " +
            "join Curriculum cs on c.curriculumId = c.id and c.status = 'ACTIVE' " +
            "join Subject s on c.subjectId = s.id and s.status = 'ACTIVE' " +
            "join Semester sm on c.semesterId = sm.id and sm.status = 'ACTIVE' " +
            "LEFT JOIN Users uc ON c.createdBy = uc.id " +
            "LEFT JOIN Users um ON c.modifiedBy = um.id " +
            "where (:search is null or cs.curriculumName like %:search%) " +
            "ORDER BY c.createdAt DESC")
    Page<CurriculumSubjectDto> findCurriculumSubject(String search, Pageable pageable);

    @Query(value = "select cs.subject_id from users u " +
            " join roles r on r.id = u.role_id and r.status = 'ACTIVE' " +
            " join student_curriculum sc on sc.student_id = u.id and sc.status='ACTIVE' " +
            " join curriculum_sub cs on cs.curriculum_id = sc.curriculum_id and cs.status='ACTIVE' " +
            " where r.role_name = 'STUDENT' and u.id = :id ",nativeQuery = true)
    List<Integer> findSubjectIdsByStudentId(Integer id);

    @Query(value = "select * from curriculum_sub cs " +
            " where cs.status = 'ACTIVE' and cs.curriculum_id = :curriculumId and cs.subject_id = :subjectId ",nativeQuery = true)
    CurriculumSubject findByCurriculuAndSubject(Integer curriculumId, Integer subjectId);

    @Query(value = "select cs.* from curriculum_sub cs where cs.subject_id = :subjectId and cs.status = 'ACTIVE' ",nativeQuery = true)
    List<CurriculumSubject> findBySubject(Integer subjectId);
}


