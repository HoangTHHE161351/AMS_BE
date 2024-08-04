package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.attendance.model.ClassSubject;
import vn.attendance.service.classSubject.response.ClassSubjectDto;
import vn.attendance.service.classSubject.response.IClassDto;

import java.util.List;
import java.util.Optional;
@Repository
public interface ClassSubjectRepository extends JpaRepository<ClassSubject, Integer> {

    @Query("select c from ClassSubject c " +
            "where c.classId = :classId and c.subjectId = :subId and c.status = 'ACTIVE'")
    ClassSubject findByClassAndSubId(@Param("classId") Integer classId,
                                     @Param("subId") Integer subId);



    @Query("select c from ClassSubject c where c.id = :id")
    Optional<ClassSubject> findById(Integer id);

    @Query(value = "select new vn.attendance.service.classSubject.response.ClassSubjectDto(cs.id, cr.className, s.name, cs.status, cs.createdAt, uc.username, cs.modifiedAt, um.username) " +
            "from ClassSubject cs " +
            "LEFT JOIN Users uc ON cs.createdBy = uc.id " +
            "LEFT JOIN Users um ON cs.modifiedBy = um.id " +
            "LEFT JOIN ClassRoom cr ON cs.classId = cr.id " +
            "LEFT JOIN Subject s ON cs.subjectId = s.id " +
            "where (:search is null or cr.className like %:search% " +
            "or s.name like %:search%) order by cs.createdAt desc",
            countQuery = "select count(cs) from ClassSubject cs " +
                    "LEFT JOIN ClassRoom cr ON cs.classId = cr.id " +
                    "LEFT JOIN Subject s ON cs.subjectId = s.id " +
                    "where (:search is null or cr.className like %:search% or s.name like %:search%)")
    Page<ClassSubjectDto> findClassSubject(String search, Pageable pageable);

    @Query("select c from ClassSubject c order by c.createdAt desc ")
    List<ClassSubject> findAllClassSubject();

    @Query(value = "select c.subject_id from class_subject c where c.class_id = :classId and c.status = 'ACTIVE' ", nativeQuery = true)
    List<Integer> findSubjectIdsByClassId(Integer classId);

    @Query(value = "select cs.* from class_subject cs where cs.subject_id = :subjectId and cs.status = 'ACTIVE' ",nativeQuery = true)
    List<ClassSubject> findBySubject(Integer subjectId);

    @Query(value = "select cs.id as id, " +
            " cs.class_id as classId, " +
            " c.class_name as name, " +
            " c. description as description " +
            " from class_subject cs " +
            " inner join class_room c on c.id = cs.class_id " +
            " inner join subjects s on s.id = cs.subject_id " +
            " where cs.status = 'ACTIVE' and cs.subject_id = :subjectId", nativeQuery = true)
    List<IClassDto> getClassesSubject(Integer subjectId);

    @Query(value = "select * from class_subject cs " +
            " where cs.class_id = :classId and cs.subject_id = :subjectId and cs.status = 'ACTIVE' ", nativeQuery = true)
    ClassSubject findByClassAndSubject(Integer classId, Integer subjectId);
}