package vn.attendance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.attendance.model.Users;
import vn.attendance.service.mqtt.entity.UserDto;
import vn.attendance.service.user.service.request.IUserDto;
import vn.attendance.service.user.service.request.IUserFace;
import vn.attendance.service.user.service.response.UsersDto;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    @Query("select u from Users u where u.username = :username and u.status <> 'DELETED'")
    Optional<Users> findByUsername(String username);

    @Query("select u from Users u where (u.username = :username or u.email = :username) and u.status <> 'DELETED'")
    Optional<Users> findByUsernameOrEmail(String username);

    @Query("select u from Users u where u.email = :email and u.status <> 'DELETED'")
    Optional<Users> findByEmail(String email);

    @Query("select u from Users u where u.phone = :phone and u.status <> 'DELETED'")
    Optional<Users> findByPhone(String phone);

    @Query("select new vn.attendance.service.user.service.response.UsersDto(u.id, r.roleName, u.email,u.username, u.firstName," +
            " u.lastName, u.avata, case u.gender when 1 then 'MALE' when 2 then 'FEMALE' else 'OTHER' end, u.address, u.phone, u.dob, u.description, u.status, u.createdAt, uc.username," +
            " u.modifiedAt, um.username )" +
            " FROM Users u" +
            " JOIN Role r ON u.roleId = r.id" +
            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id " +
            "where r.roleName <> 'ADMIN' and (:roleName is null or r.roleName = :roleName) " +
            "and (:status is null or u.status = :status) " +
            "and (:gender is null or u.gender = :gender) " +
            "and (:search is null or u.username like %:search% " +
            "or u.firstName like %:search% " +
            "or u.lastName like %:search% " +
            "or u.address like %:search% " +
            "or u.phone like %:search% ) order by u.createdAt desc")
    Page<UsersDto> searchUser(@Param(value = "search") String search,
                              @Param(value = "status") String status,
                              @Param(value = "roleName") String roleName,
                              @Param(value = "gender") Integer gender,
                              Pageable pageable);

    @Query("select new vn.attendance.service.user.service.response.UsersDto(u.id, r.roleName, u.email,u.username, u.firstName," +
            " u.lastName, f.image, case u.gender when 1 then 'MALE' when 2 then 'FEMALE' else 'OTHER' end, u.address, u.phone, u.dob, u.description, u.status, u.createdAt, uc.username," +
            " u.modifiedAt, um.username )" +
            " FROM Users u" +
            " JOIN Role r ON u.roleId = r.id" +
            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id " +
            " LEFT JOIN Facial f ON u.id = f.userId and f.status = 'ACTIVE' " +
            "where r.roleName <> 'ADMIN' and (:roleName is null or r.roleName = :roleName) " +
            "and (:status is null or u.status = :status) " +
            "and (:gender is null or u.gender = :gender) and (:search is null " +
            "or u.username like %:search% " +
            "or u.firstName like %:search% " +
            "or u.lastName like %:search% " +
            "or u.address like %:search% " +
            "or u.phone like %:search% ) order by u.createdAt desc")
    List<UsersDto> findUsersByConditions(@Param(value = "search") String search,
                                         @Param(value = "status") String status,
                                         @Param(value = "roleName") String roleName,
                                         @Param(value = "gender") Integer gender);

    @Query("SELECT new vn.attendance.service.user.service.response.UsersDto(u.id, r.roleName, u.email,u.username, u.firstName," +
            " u.lastName, u.avata, case u.gender when 1 then 'MALE' when 2 then 'FEMALE' else 'OTHER' end, u.address, u.phone, u.dob, u.description, u.status, u.createdAt, uc.username," +
            " u.modifiedAt, um.username )" +
            " FROM Users u" +
            " JOIN Role r ON u.roleId = r.id" +
            " LEFT JOIN Users uc ON u.createdBy = uc.id" +
            " LEFT JOIN Users um ON u.modifiedBy = um.id where u.roleId <> 1" +
            " order by u.createdAt desc")
    Page<UsersDto> findAllDto(Pageable pageable);

    @Query("select u from Users u where u.accessToken = :token and u.status = 'PENDING_ACTIVATION'")
    Optional<Users> findByToken(@Param(value = "token") String token);

    @Query("select u from Users u join Role r on u.roleId = r.id where r.roleName = :roleName")
    List<Users> findByRoleName(@Param(value = "roleName") String roleName);

    @Query( value = "SELECT u.id as id, r.role_name as roleName, u.email as email, u.user_name as userName, u.first_name as firstName, " +
            "u.last_name as lastName, u.avata as avata, case u.gender when 1 then 'MALE' when 2 then 'FEMALE' else 'OTHER' end as gender," +
            " u.address as address, u.phone as phone, u.dob as dob," +
            " u.description as description, u.status as status, u.created_at as createAt, uc.user_name as createdBy, " +
            "u.modified_at as modifiedAt, um.user_name as modifiedBy " +
            "FROM users u " +
            "JOIN roles r ON u.role_id = r.id " +
            "LEFT JOIN users uc ON u.created_by = uc.id " +
            "LEFT JOIN users um ON u.modified_by = um.id " +
            "WHERE u.role_id <> 1 " +
            "AND (:roleName IS NULL OR r.role_name = :roleName) " +
            "AND u.status = 'INACTIVE' " +
            "AND (:date IS NULL OR DATE(u.modified_at) = :date) " +
            "AND (:search IS NULL OR u.user_name LIKE CONCAT('%', :search, '%') " +
            "OR u.first_name LIKE CONCAT('%', :search, '%') " +
            "OR u.last_name LIKE CONCAT('%', :search, '%') " +
            "OR u.address LIKE CONCAT('%', :search, '%') " +
            "OR u.phone LIKE CONCAT('%', :search, '%')) " +
            "ORDER BY u.created_at DESC",
            countQuery =  "SELECT u.id as id, r.role_name as roleName, u.email as email, u.user_name as userName, u.first_name as firstName, " +
            "u.last_name as lastName, u.avata as avata, case u.gender when 1 then 'MALE' when 2 then 'FEMALE' else 'OTHER' end as gender," +
            " u.address as address, u.phone as phone, u.dob as dob," +
            " u.description as description, u.status as status, u.created_at as createAt, uc.user_name as createdBy, " +
            "u.modified_at as modifiedAt, um.user_name as modifiedBy " +
            "FROM users u " +
            "JOIN roles r ON u.role_id = r.id " +
            "LEFT JOIN users uc ON u.created_by = uc.id " +
            "LEFT JOIN users um ON u.modified_by = um.id " +
            "WHERE u.role_id <> 1 " +
            "AND (:roleName IS NULL OR r.role_name = :roleName) " +
            "AND u.status = 'INACTIVE' " +
            "AND (:date IS NULL OR DATE(u.modified_at) = :date) " +
            "AND (:search IS NULL OR u.user_name LIKE CONCAT('%', :search, '%') " +
            "OR u.first_name LIKE CONCAT('%', :search, '%') " +
            "OR u.last_name LIKE CONCAT('%', :search, '%') " +
            "OR u.address LIKE CONCAT('%', :search, '%') " +
            "OR u.phone LIKE CONCAT('%', :search, '%')) " +
            "ORDER BY u.created_at DESC", nativeQuery = true)
    Page<IUserDto> findUsersBlackList(@Param("search") String search,
                                      @Param("date") LocalDate date,
                                      @Param("roleName") String roleName,
                                      Pageable pageable);

    @Query("select u from Users u where u.id = :id and u.status <> 'DELETED'")
    Optional<Users> findUsersById(Integer id);

    @Query("select u.id as id, " +
            " u.username as username, " +
            " u.dob as dob, " +
            " u.status as status , " +
            " concat(u.lastName, ' ', u.firstName) as fullname, " +
            " u.gender as gender, " +
            " u.address as address, " +
            " f.image as image" +
            " from Users u join Role r on u.roleId = r.id " +
            " join Facial f on u.id = f.userId " +
            " WHERE r.roleName = :roleName" +
            " AND u.status <> 'DELETED' " +
            " AND f.status <> 'DELETE'")
    List<IUserFace> findUserByRole(@Param(value = "roleName")String roleName);

    @Modifying
    @Transactional
    @Query("update Users u set u.faceId = :faceId where u.id = :id")
    void setFaceId(@Param("id") Integer id, @Param("faceId") String faceId);

    @Query("select u.id as id, " +
            " u.username as username, " +
            " u.gender as gender, " +
            " concat(u.lastName, ' ', u.firstName) as fullName, " +
            " f.image as pic" +
            " from  Users u" +
            " join Facial f on u.id = f.userId " +
            " where u.status = 'ACTIVE' " +
            " and f.status = 'ACTIVE' ")
    List<UserDto> getUserFace();

    @Query("select u.id as id, " +
            " u.username as username, " +
            " u.gender as gender, " +
            " concat(u.lastName, ' ', u.firstName) as fullName, " +
            " f.image as pic" +
            " from  Users u " +
            " join Facial f on u.id = f.userId " +
            " where u.id = :userId " +
            " and u.status = 'ACTIVE' " +
            " and f.status = 'ACTIVE' ")
    UserDto getUserFace(Integer userId);


    @Query(value = "SELECT u.id " +
            "FROM Users u " +
            "JOIN Roles r ON u.role_id = r.id " +
            "WHERE r.role_name = :roleName " +
            "AND u.status = 'ACTIVE'", nativeQuery = true)
    List<Integer> findUserIdByRole(@Param(value = "roleName") String roleName);


}