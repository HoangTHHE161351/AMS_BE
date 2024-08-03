package vn.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.attendance.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("select r from Role r where r.id = :id and r.status = 'ACTIVE'")
    Optional<Role> findById(Integer id);
    @Query("select r from Role r where r.roleName = :role and r.status = 'ACTIVE'")
    Optional<Role> findByRoleName(String role);

    @Query("select r from Role r where r.groupId = :groupId and r.status = 'ACTIVE'")
    Role getByGroupId(String groupId);

    @Query("select r from Role r where r.status = 'ACTIVE' and r.roleName <> 'ADMIN'")
    List<Role> dropdownRoles();
}