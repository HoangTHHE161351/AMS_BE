package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.attendance.model.Users;
import vn.attendance.service.student.response.StudentDto;
import vn.attendance.service.teacher.service.response.TeacherDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Users, Integer> {
    @Query("select new vn.attendance.service.student.response.StudentDto(u.id, u.email,u.username, concat(u.firstName,' ',u.lastName)," +
            " c.curriculumName, u.avata, u.gender, u.address, u.phone, u.dob, u.status, u.createdAt, concat(uc.firstName,' ',uc.lastName)," +
            " u.modifiedAt, concat(um.firstName,' ',um.lastName) )" +
            " FROM Users u" +
            " JOIN Role r ON u.roleId = r.id" +
            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id " +
            " LEFT JOIN StudentCurriculum sc ON u.id = sc.studentId " +
            " LEFT JOIN Curriculum c ON sc.courseId = c.id " +
            "where r.roleName = 'STUDENT' " +
            "and (:status is null or u.status = :status) " +
            "and (:curriculumName is null or c.curriculumName = :curriculumName) " +
            "and (:search is null " +
            "or u.username like %:search% " +
            "or u.firstName like %:search% " +
            "or u.lastName like %:search% " +
            "or u.address like %:search% " +
            "or u.phone like %:search% ) order by u.createdAt desc")
    List<StudentDto> findStudentByConditions(String search, String status, String curriculumName);

    @Query("select new vn.attendance.service.student.response.StudentDto(u.id, u.email,u.username, concat(u.firstName,' ',u.lastName)," +
            " c.curriculumName, u.avata, u.gender, u.address, u.phone, u.dob, u.status, u.createdAt, concat(uc.firstName,' ',uc.lastName)," +
            " u.modifiedAt, concat(um.firstName,' ',um.lastName) )" +
            " FROM Users u" +
            " JOIN Role r ON u.roleId = r.id" +
            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id " +
            " LEFT JOIN StudentCurriculum sc ON u.id = sc.studentId " +
            " LEFT JOIN Curriculum c ON sc.courseId = c.id and c.status = 'ACTUVE' " +
            " where r.roleName = 'STUDENT' " +
            "AND (:search IS NULL OR u.username LIKE CONCAT('%', :search, '%')) " +
            " AND (:id is null or u.id = :id)")
    Optional<StudentDto> findStudentById(Integer id);


    @Query("select u from Users u join Role r on u.roleId = r.id and r.status = 'ACTIVE'" +
            " where r.roleName = 'STUDENT' and u.status = 'ACTIVE'")
    List<Users> findAllStudent();

    @Query("SELECT u.id, u.username, u.roleId, u.email, u.password, concat(u.lastName, ' ', u.firstName) as fullname, " +
            "u.gender, u.address, u.phone, u.dob, u.status, u.createdAt, c.curriculumName " +
            "FROM Users u LEFT JOIN Role r ON u.roleId = r.id " +
            "LEFT JOIN StudentCurriculum sc ON u.id = sc.studentId " +
            "LEFT JOIN Curriculum c ON sc.id = c.id " +
            "       WHERE r.roleName = 'STUDENT'")
    Page<Users> findStudent(Pageable pageable);

    @Query("SELECT u FROM Users u JOIN StudentCurriculum sc ON u.id = sc.studentId JOIN Curriculum c ON sc.courseId = c.id " +
            "JOIN Role r ON u.roleId = r.id " +
            "WHERE (:search is null or u.username LIKE %:search% " +
            "OR u.firstName LIKE %:search% " +
            "OR u.lastName LIKE %:search% " +
            "OR u.address LIKE %:search% " +
            "OR u.phone LIKE %:search%) " +
            "AND r.roleName = 'STUDENT' " +
            "AND (:curriculumName IS NULL OR c.curriculumName = :curriculumName) " +
            "AND (:status IS NULL OR u.status = :status) order by u.createdAt desc")
    Page<Users> findStudentby(@Param(value = "search") String search,
                              @Param(value = "status") String status,
                              @Param(value = "curriculumName") String curriculumName,
                              Pageable pageable);
}
