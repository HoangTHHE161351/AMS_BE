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
            " c.curriculumName, u.avata, u.gender, u.address, u.phone, u.dob, u.status, u.createdAt, concat(uc.firstName,' ',uc.lastName)," +
            " u.modifiedAt, concat(um.firstName,' ',um.lastName) )" +
            " FROM Users u" +
            " JOIN Role r ON u.roleId = r.id" +
            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id " +
            " LEFT JOIN StudentCurriculum sc ON u.id = sc.studentId " +
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
            " c.curriculumName, u.avata, u.gender, u.address, u.phone, u.dob, u.status, u.createdAt, concat(uc.firstName,' ',uc.lastName)," +
            " u.modifiedAt, concat(um.firstName,' ',um.lastName) )" +
            " FROM Users u" +
            " JOIN Role r ON u.roleId = r.id" +
            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id " +
            " LEFT JOIN StudentCurriculum sc ON u.id = sc.studentId " +
            " LEFT JOIN Curriculum c ON sc.curriculumId = c.id and c.status = 'ACTUVE' " +
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

    @Query("SELECT new vn.attendance.service.user.service.response.UsersDto(u.id, r.roleName, u.email,u.username, u.firstName," +
            " u.lastName, u.avata, case u.gender when 1 then 'MALE' when 2 then 'FEMALE' else 'OTHER' end, u.address, u.phone, " +
            "u.dob, u.description, u.status, u.createdAt, uc.username," +
            " u.modifiedAt, um.username ) " +
            "FROM Users u " +
            " LEFT JOIN StudentCurriculum sc ON u.id = sc.studentId " +
            " LEFT JOIN Curriculum c ON sc.curriculumId = c.id " +
            " JOIN Role r ON u.roleId = r.id and r.status = 'ACTIVE' " +
            " LEFT JOIN Users uc ON u.createdBy = uc.id and uc.status = 'ACTIVE'" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id and um.status = 'ACTIVE' " +
            "WHERE (:search is null or u.username LIKE %:search% " +
            "OR u.firstName LIKE %:search% " +
            "OR u.lastName LIKE %:search% " +
            "OR u.address LIKE %:search% " +
            "OR u.phone LIKE %:search%) " +
            "AND r.roleName = 'STUDENT' " +
            "AND (:curriculumName IS NULL OR c.curriculumName = :curriculumName) " +
            "AND (:status IS NULL OR u.status = :status) order by u.createdAt desc")
    Page<UsersDto> findStudentby(@Param(value = "search") String search,
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
}
