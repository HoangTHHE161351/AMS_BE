package vn.attendance.service.role.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.attendance.model.Role;
import vn.attendance.repository.RoleRepository;
import vn.attendance.service.role.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Role> dropdownRoles() {
        return roleRepository.dropdownRoles();
    }
}
