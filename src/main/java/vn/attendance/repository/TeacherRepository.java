package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.attendance.model.Users;
import vn.attendance.service.mqtt.entity.UserDto;
import vn.attendance.service.teacher.response.ITeacherDto;
import vn.attendance.service.teacher.response.TeacherDto;
import vn.attendance.service.user.service.response.UsersDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Users, Integer> {

    @Query("select new vn.attendance.service.user.service.response.UsersDto(u.id, r.roleName, u.email,u.username, u.firstName," +
            " u.lastName, u.avata, case u.gender when 1 then 'MALE' when 2 then 'FEMALE' else 'OTHER' end, u.address, u.phone, u.dob, u.description, u.status, u.createdAt, uc.username," +
            " u.modifiedAt, um.username )" +
            " FROM Users u" +
            " JOIN Role r ON u.roleId = r.id" +
            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id " +
            "where r.roleName <> 'ADMIN' and r.roleName = 'TEACHER' " +
            "and (coalesce(:status, '') = '' or u.status = :status) " +
            "and (coalesce(:gender, '') = '' or u.gender = :gender) " +
            "and (coalesce(:search, '') = '' or u.username like %:search% " +
            "or u.firstName like %:search% " +
            "or u.lastName like %:search% " +
            "or u.address like %:search% " +
            "or u.phone like %:search% ) order by u.createdAt desc")
    Page<UsersDto> findTeacher(@Param(value = "search") String search,
                               @Param(value = "status") String status,
                               @Param(value = "gender") Integer gender,
                               Pageable pageable);

    @Query("SELECT new vn.attendance.service.teacher.response.TeacherDto( u.id,u.roleId, u.email, u.username, " +
            "concat(u.firstName, ' ', u.lastName)," +
            "u.avata,u.gender,u.address,u.phone,u.dob,u.status, u.createdAt," +
            "uc.username, u.modifiedAt, um.username, u.description,c.className)" +
            " FROM Users u " +
            " join Role r on u.roleId = r.id and r.status = 'ACTIVE' and r.roleName = 'TEACHER'" +
            " join Schedule s on u.id = s.id and u.status = 'ACTIVE'" +
            " join ClassRoom c on s.classId = c.id and c.status = 'ACTIVE'" +
            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id" +
            " WHERE u.id = :id " +
            "ORDER BY u.createdAt DESC")
    List<TeacherDto> teacherByClassRoom(Integer id);

//    @Query("SELECT new vn.attendance.service.teacher.service.response.TeacherDto( u.id,u.roleId, u.email, u.username, " +
//            "concat(u.firstName, ' ', u.lastName)," +
//            "u.avata,u.gender,u.address,u.phone,u.dob,u.status, u.createdAt," +
//            "uc.username, u.modifiedAt, um.username, u.description,sb.code)" +
//            " FROM Users u " +
//            " join Role r on u.roleId = r.id and r.status = 'ACTIVE' and r.roleName = 'TEACHER'" +
//            " join Schedule s on u.id = s.id and u.status = 'ACTIVE'" +
//            " join Subject sb on s.subjectId = sb.id and sb.status = 'ACTIVE'" +
//            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
//            " LEFT JOIN Users um ON u.modifiedBy = um.id" +
//            " WHERE u.id= :id " +
//            "ORDER BY u.createdAt DESC")
//    List<TeacherDto> teacherBySubject(Integer id);


    @Query("SELECT new vn.attendance.service.teacher.response.TeacherDto( u.id,u.roleId, u.email, u.username, " +
            "concat(u.firstName, ' ', u.lastName)," +
            "u.avata,u.gender,u.address,u.phone,u.dob,u.status, u.createdAt," +
            "uc.username, u.modifiedAt, um.username, u.description,sb.name)" +
            " FROM Users u " +
            "JOIN Role r ON u.roleId = r.id AND r.status = 'ACTIVE' AND r.roleName = 'TEACHER' " +
            "JOIN TeacherSubject ts ON u.id = ts.teacherId AND ts.status = 'ACTIVE' " +
            "JOIN Subject sb ON ts.subjectId = sb.id AND sb.status = 'ACTIVE' " +
            "LEFT JOIN Users uc ON u.createdBy = uc.id " +
            "LEFT JOIN Users um ON u.modifiedBy = um.id " +
            "WHERE u.id = :id " +
            "ORDER BY u.createdAt DESC")
    List<TeacherDto> teacherBySubject(Integer id);


    @Query("select u from Users u join Role r on u.roleId = r.id and r.status = 'ACTIVE'" +
            " where r.roleName = 'TEACHER' and u.status = 'ACTIVE'")
    List<Users> findAllRoleTeacher();

    @Query("select u.id as id, " +
            " u.username as username, " +
            " u.gender as gender, " +
            " concat(u.lastName, ' ', u.firstName) as fullName," +
            " f.image as pic" +
            " from Users u join Facial f on u.id = f.userId " +
            " where u.id = :userId " +
            " and u.status = 'ACTIVE' " +
            " and f.status = 'ACTIVE' ")
    UserDto getTecherById(Integer userId);

    @Query(value = "select u.id as id, " +
            " concat(u.last_name,' ', u.first_name) as name, " +
            " u.user_name as username " +
            " FROM Users u " +
            " JOIN roles r ON u.role_id = r.id " +
            " JOIN teacher_subject ts ON u.id = ts.teacher_id " +
            " where r.role_name = 'TEACHER' " +
            " and (:subjectId is null or ts.subject_id = :subjectId) " +
            " and u.status = 'ACTIVE' " +
            " and ts.status = 'ACTIVE' ", nativeQuery = true)
    List<ITeacherDto> getTeacherDropList(Integer subjectId);

    @Query(value = "select u.* from Users u join Roles r on u.role_id = r.id and r.status = 'ACTIVE'" +
            " where r.role_name = 'TEACHER' and u.status = 'ACTIVE' and u.user_name = :teacherCode", nativeQuery = true)
    Users findTeacherByCode(String teacherCode);

    @Query(value = "select u.id as id, " +
            " concat(u.last_name,' ', u.first_name) as name, " +
            " u.user_name as username " +
            " FROM Users u " +
            " JOIN roles r ON u.role_id = r.id " +
            " JOIN teacher_subject ts ON u.id = ts.teacher_id " +
            " where r.role_name = 'TEACHER' " +
            " and (:subjectId is null or ts.subject_id = :subjectId) " +
            "AND NOT EXISTS (SELECT 1 FROM schedules s WHERE s.user_id = u.id AND s.time_slot_id = :slotId and Date(s.learn_timestamp) = Date(:date) and s.status = 'ACTIVE') " +
            " and u.status = 'ACTIVE' " +
            " and ts.status = 'ACTIVE' ", nativeQuery = true)
    List<ITeacherDto> getTeacherForSchedule(Integer subjectId, LocalDate date, Integer slotId);
}
