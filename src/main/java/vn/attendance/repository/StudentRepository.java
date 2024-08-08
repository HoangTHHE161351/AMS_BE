package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.attendance.model.Users;
import vn.attendance.service.mqtt.entity.UserDto;
import vn.attendance.service.student.response.IDropdownStudentDto;
import vn.attendance.service.student.response.StudentCurriculumDto;
import vn.attendance.service.student.response.StudentDto;
import vn.attendance.service.user.service.response.UsersDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Users, Integer> {
    @Query("select new vn.attendance.service.student.response.StudentDto(u.id, u.email,u.username, concat(u.firstName,' ',u.lastName)," +
            " fa.image, u.gender, u.address, u.phone, u.dob, u.status, u.createdAt, concat(uc.firstName,' ',uc.lastName)," +
            " u.modifiedAt, concat(um.firstName,' ',um.lastName), c.id, c.curriculumName )" +
            " FROM Users u" +
            " JOIN Role r ON u.roleId = r.id" +
            " LEFT JOIN Facial fa ON fa.userId = u.id and fa.status = 'ACTIVE' " +
            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id " +
            " LEFT JOIN StudentCurriculum sc ON u.id = sc.studentId and sc.status = 'ACTIVE' " +
            " LEFT JOIN Curriculum c ON sc.curriculumId = c.id " +
            "where r.roleName = 'STUDENT' " +
            "and (:status is null or u.status = :status) " +
            "and (:curriculumName is null or c.curriculumName = :curriculumName) " +
            "and (:search is null " +
            "or u.username like %:search% " +
            "or u.firstName like %:search% " +
            "or u.email like %:search% " +
            "or u.lastName like %:search% " +
            "or u.address like %:search% " +
            "or u.phone like %:search% ) order by u.createdAt desc")
    List<StudentDto> findStudentByConditions(String search, String status, String curriculumName);

    @Query("select new vn.attendance.service.student.response.StudentDto(u.id, u.email,u.username, concat(u.firstName,' ',u.lastName)," +
            " fa.image, u.gender, u.address, u.phone, u.dob, u.status, u.createdAt, concat(uc.firstName,' ',uc.lastName)," +
            " u.modifiedAt, concat(um.firstName,' ',um.lastName), c.id, c.curriculumName )" +
            " FROM Users u" +
            " JOIN Role r ON u.roleId = r.id" +
            " LEFT JOIN Facial fa ON fa.userId = u.id and fa.status = 'ACTIVE' " +
            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id " +
            " LEFT JOIN StudentCurriculum sc ON u.id = sc.studentId and sc.status = 'ACTIVE' " +
            " LEFT JOIN Curriculum c ON sc.curriculumId = c.id " +
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

    @Query(value = "select new vn.attendance.service.student.response.StudentDto(u.id, u.email, u.username, concat(u.firstName, ' ', u.lastName), " +
            "fa.image, u.gender, u.address, u.phone, u.dob, u.status, u.createdAt, concat(uc.firstName, ' ', uc.lastName), " +
            "u.modifiedAt, concat(um.firstName, ' ', um.lastName), c.id, c.curriculumName) " +
            "FROM Users u " +
            "LEFT JOIN StudentCurriculum sc ON u.id = sc.studentId and sc.status = 'ACTIVE' " +
            "LEFT JOIN Curriculum c ON sc.curriculumId = c.id " +
            "LEFT JOIN Facial fa ON fa.userId = u.id and fa.status = 'ACTIVE' " +
            "JOIN Role r ON u.roleId = r.id and r.status = 'ACTIVE' " +
            "LEFT JOIN Users uc ON u.createdBy = uc.id and uc.status = 'ACTIVE' " +
            "LEFT JOIN Users um ON u.modifiedBy = um.id and um.status = 'ACTIVE' " +
            "WHERE (:search is null or u.username LIKE %:search% " +
            "OR u.firstName LIKE %:search% " +
            "OR u.lastName LIKE %:search% " +
            "OR u.address LIKE %:search% " +
            "OR u.phone LIKE %:search%) " +
            "AND r.roleName = 'STUDENT' " +
            "AND (:curriculumName IS NULL OR c.curriculumName = :curriculumName) " +
            "AND (:status IS NULL OR u.status = :status) " +
            "ORDER BY u.createdAt DESC",
            countQuery = "select count(u.id) FROM Users u " +
                    "LEFT JOIN StudentCurriculum sc ON u.id = sc.studentId and sc.status = 'ACTIVE' " +
                    "LEFT JOIN Curriculum c ON sc.curriculumId = c.id " +
                    "JOIN Role r ON u.roleId = r.id and r.status = 'ACTIVE' " +
                    "WHERE (:search is null or u.username LIKE %:search% " +
                    "OR u.firstName LIKE %:search% " +
                    "OR u.lastName LIKE %:search% " +
                    "OR u.address LIKE %:search% " +
                    "OR u.phone LIKE %:search%) " +
                    "AND r.roleName = 'STUDENT' " +
                    "AND (:curriculumName IS NULL OR c.curriculumName = :curriculumName) " +
                    "AND (:status IS NULL OR u.status = :status)")
    Page<StudentDto> findStudentby(@Param(value = "search") String search,
                                   @Param(value = "status") String status,
                                   @Param(value = "curriculumName") String curriculumName,
                                   Pageable pageable);


    @Query("SELECT new vn.attendance.service.student.response.StudentCurriculumDto(c.id, cs.curriculumName, u.username, concat(u.firstName, ' ', u.lastName)," +
            "c.description, c.status, c.createdAt, uc.username, c.modifiedAt, um.username) " +
            "FROM StudentCurriculum c " +
           "JOIN Curriculum cs ON c.curriculumId = cs.id " +
            "LEFT JOIN Users u ON c.studentId = u.id " +
            "LEFT JOIN Users uc ON c.createdBy = uc.id " +
            "LEFT JOIN Users um ON c.modifiedBy = um.id " +
            "WHERE (:search IS NULL OR cs.curriculumName LIKE %:search%) " +
            "ORDER BY c.createdAt DESC")
    Page<StudentCurriculumDto> findStudentCurriculum(String search, Pageable pageable);

    @Query("select u.id as id, " +
            " u.username as username, " +
            " u.gender as gender, " +
            " concat(u.lastName, ' ', u.firstName) as fullName," +
            " f.image as pic" +
            " from StudentClass sc" +
            " join Users u on sc.studentId = u.id " +
            " join Facial f on u.id = f.userId " +
            " where sc.classId = :classId " +
            " and sc.status = 'ACTIVE' " +
            " and u.status = 'ACTIVE' " +
            " and f.status = 'ACTIVE' ")
    List<UserDto> getStudentByClass(Integer classId);

    @Query(value = "select u.* from Users u " +
            " join Roles r on u.role_id = r.id " +
            " where u.user_name = :username and r.role_name = 'STUDENT'and u.status = 'ACTIVE'", nativeQuery = true)
    Users findStudentByUsername(String username);

    @Query(value = "select u.* from Users u join Roles r " +
            " on r.role_name = 'STUDENT' and r.status = 'ACTIVE' and r.id=u.role_id " +
            " where u.id = :id and u.status = 'ACTIVE' ",nativeQuery = true)
    Users getStudentById(Integer id);

    @Query(value = "select s.id as id, " +
            " s.user_name as studentCode, " +
            " concat(s.last_name,' ', s.first_name) as fullName " +
            " from Users s " +
            " inner join Roles r on s.role_id = r.id and r.role_name = 'STUDENT' " +
            " where (:search is null or s.user_name LIKE concat('%',:search,'%') " +
            " OR s.first_name LIKE concat('%',:search,'%') " +
            " OR s.last_name LIKE concat('%',:search,'%') " +
            " OR s.email LIKE concat('%',:search,'%') " +
            " OR s.phone LIKE concat('%',:search,'%')) ", nativeQuery = true)
    List<IDropdownStudentDto> dropdownStudent(String search);

    @Query(value = "SELECT u.id as id, u.user_name as studentCode, CONCAT(u.last_name, ' ', u.first_name) as fullName " +
            " FROM Users u " +
            " INNER JOIN student_curriculum sc ON u.id = sc.student_id and sc.status = 'ACTIVE' " +
            " INNER JOIN curriculum_sub cs ON cs.curriculum_id = sc.curriculum_id and cs.status = 'ACTIVE' " +
            " INNER JOIN class_subject crs ON crs.subject_id = cs.subject_id and crs.status = 'ACTIVE' " +
            " WHERE crs.id = :classId " +
            " AND NOT EXISTS (SELECT 1 FROM student_class sc2 WHERE sc2.class_id = :classId AND sc2.student_id = u.id And sc2.status = 'ACTIVE') " +
            " And (:search is null or u.user_name LIKE concat('%',:search,'%') " +
            " OR u.first_name LIKE concat('%',:search,'%') " +
            " OR u.last_name LIKE concat('%',:search,'%') " +
            " OR u.email LIKE concat('%',:search,'%') " +
            " OR u.phone LIKE concat('%',:search,'%')) order by u.id", nativeQuery = true)
    List<IDropdownStudentDto> dropdownStudentToClass(String search, Integer classId);
}
