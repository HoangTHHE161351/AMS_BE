package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.attendance.model.StudentClass;
import vn.attendance.service.studentClass.response.StudentClassDto;

import java.util.List;
import java.util.Optional;
@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass, Integer> {

    @Query("select c from StudentClass c where c.id = :id")
    Optional<StudentClass> findById(Integer id);

    @Query("select c from StudentClass c where c.studentId = :studentId and c.classId = :classId")
    Optional<StudentClass> findByStudentIdAndClassId(Integer studentId, Integer classId);

    @Query("select c from StudentClass c where c.studentId = :studentId")
    Optional<StudentClass> findByStudentId(Integer studentId);

    @Query("select c from StudentClass c where c.classId = :classId")
    Optional<StudentClass> findByClassId(Integer classId);

    @Query("select c from StudentClass c where c.studentId = :studentId and c.classId = :classId and c.status = :status")
    Optional<StudentClass> findByStudentIdAndClassIdAndStatus(Integer studentId, Integer classId, String status);

    @Query(value = "select new vn.attendance.service.studentClass.response.StudentClassDto(sc.id, u.username, cr.className, sc.status, sc.createdAt, uc.username, sc.modifiedAt, um.username) " +
            "from StudentClass sc " +
            "LEFT JOIN Users uc ON sc.createdBy = uc.id " +
            "LEFT JOIN Users um ON sc.modifiedBy = um.id " +
            "LEFT JOIN ClassRoom cr ON sc.classId = cr.id " +
            "LEFT JOIN Users u ON sc.studentId = u.id " +
            "where (:search is null or cr.className like %:search% or u.username like %:search%) order by sc.createdAt desc",
            countQuery = "select count(sc) from StudentClass sc " +
                    "LEFT JOIN ClassRoom cr ON sc.classId = cr.id " +
                    "LEFT JOIN Users u ON sc.studentId = u.id " +
                    "where (:search is null or cr.className like %:search% or u.username like %:search%)")
    Page<StudentClassDto> findStudentClass(String search, Pageable pageable);

    @Query("select c from StudentClass c where c.classId = :classId and c.status = 'ACTIVE'")
    List<StudentClass> findAllStudentClassByClassId(Integer classId);

}