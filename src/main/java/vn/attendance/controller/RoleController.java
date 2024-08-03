package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.role.RoleService;

@RestController
@RequestMapping("api/v1/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("dropdown-role")
    public ApiResponse<?> dropdownRole() {
        return ApiResponse.okStatus(roleService.dropdownRoles());
    }
}
