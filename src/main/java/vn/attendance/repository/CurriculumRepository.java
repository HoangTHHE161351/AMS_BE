package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.model.Curriculum;
import vn.attendance.model.CurriculumSubject;
import vn.attendance.service.curriculum.response.CurriculumDto;
import vn.attendance.service.curriculum.response.ICurriculumDto;
import vn.attendance.service.curriculum.response.ISubjectDto;

import java.util.List;
import java.util.Optional;

public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {

    @Query("select c from Curriculum c where c.id = :id")
    Optional<Curriculum> findById(Integer id);

    @Query("select c from Curriculum c where c.status = 'ACTIVE'")
    List<Curriculum> findAllCurriculum();

    @Query("select new vn.attendance.service.curriculum.response.CurriculumDto(c.id, c.curriculumName, " +
            "c.description, c.status, c.createdAt, uc.username, c.modifiedAt, um.username) " +
            "from Curriculum c " +
            " LEFT JOIN Users uc ON c.createdBy = uc.id " +
            " LEFT JOIN Users um ON c.modifiedBy = um.id " +
            "where (coalesce(:search, '') = '' or c.curriculumName like %:search% ) " +
            "and (coalesce(:status, '') = '' or c.status = :status)" +
            "order by c.createdAt desc")
    Page<CurriculumDto> findCurriculum(@Param("search") String search,
                                       @Param("status") String status,
                                       Pageable pageable);

    //    delete set status = DELETE where id = :id
    @Query("update Curriculum c set c.status = 'DELETED' where c.id = :id")
    @Modifying
    @Transactional
    void deleteCurriculum(Integer id);

    @Query("select c from Curriculum c where c.curriculumName = :curriculumName and c.status = 'ACTIVE' " )
    Curriculum findCurriculumByName(String curriculumName);

    @Query(value = "select cs.id as id, " +
            " s.id as subjectId, " +
            " s.code as code, " +
            " s.name as name, " +
            " s.slots as slot " +
            " from curriculum_sub cs " +
            " inner join curriculum c on c.id = cs.curriculum_id " +
            " inner join subjects s on s.id = cs.subject_id " +
            " where cs.status = 'ACTIVE' and cs.curriculum_id = :curriculumId ", nativeQuery = true)
    List<ISubjectDto> getAllSubjectsCurriculum(Integer curriculumId);

    @Query(value = "select c.id as id, " +
            " c.curriculum_name as curriculumName , " +
            " c.description as description, " +
            " sc.status as status " +
            " from student_curriculum sc " +
            " inner join Users st on sc.student_id = st.id " +
            " inner join roles r on r.role_name='STUDENT' and r.id = st.role_id " +
            " inner join curriculum c on c.id = sc.curriculum_id " +
            " where sc.student_id = :studentId " +
            " order by sc.status ", nativeQuery = true)
    List<ICurriculumDto> getAllCurriculumsStudent(Integer studentId);

    @Query(value = "select count(*) from student_curriculum sc where sc.curriculum_id = :id and sc.status='ACTIVE' ", nativeQuery = true)
    Integer getCountStudentCurriculum(Integer id);


    @Query(value = "SELECT * FROM curriculum_sub cs WHERE cs.curriculum_id = :id and cs.status = 'ACTIVE' ", nativeQuery = true)
    List<CurriculumSubject> findAllByCurriculum(Integer id);

}