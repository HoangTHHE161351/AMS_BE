package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.attendance.model.Users;
import vn.attendance.service.teacher.service.response.TeacherDto;
import vn.attendance.service.user.service.response.UsersDto;

import java.util.List;

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
            "and (:status is null or u.status = :status) " +
            "and (:gender is null or u.gender = :gender) " +
            "and (:search is null or u.username like %:search% " +
            "or u.firstName like %:search% " +
            "or u.lastName like %:search% " +
            "or u.address like %:search% " +
            "or u.phone like %:search% ) order by u.createdAt desc")
    Page<UsersDto> findTeacher(@Param(value = "search") String search,
                               @Param(value = "status") String status,
                               @Param(value = "gender") Integer gender,
                               Pageable pageable);

    @Query("SELECT new vn.attendance.service.teacher.service.response.TeacherDto( u.id,u.roleId, u.email, u.username, " +
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

    @Query("SELECT new vn.attendance.service.teacher.service.response.TeacherDto( u.id,u.roleId, u.email, u.username, " +
            "concat(u.firstName, ' ', u.lastName)," +
            "u.avata,u.gender,u.address,u.phone,u.dob,u.status, u.createdAt," +
            "uc.username, u.modifiedAt, um.username, u.description,sb.code)" +
            " FROM Users u " +
            " join Role r on u.roleId = r.id and r.status = 'ACTIVE' and r.roleName = 'TEACHER'" +
            " join Schedule s on u.id = s.id and u.status = 'ACTIVE'" +
            " join Subject sb on s.subjectId = sb.id and sb.status = 'ACTIVE'" +
            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id" +
            " WHERE u.id= :id " +
            "ORDER BY u.createdAt DESC")
    List<TeacherDto> teacherBySubject(Integer id);

    @Query("select u from Users u join Role r on u.roleId = r.id and r.status = 'ACTIVE'" +
            " where r.roleName = 'TEACHER' and u.status = 'ACTIVE'")
    List<Users> findAllRoleTeacher();
}
