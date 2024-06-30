package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.attendance.helper.ApiResponse;
import vn.attendance.model.Subject;
import vn.attendance.model.Users;
import vn.attendance.service.subject.service.SubjectService;
import vn.attendance.service.teacher.service.TeacherService;
import vn.attendance.service.teacher.service.response.TeacherDto;
import vn.attendance.service.user.service.UserService;
import vn.attendance.service.user.service.response.UsersDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/teacher")
public class TeacherController {

    @Autowired
    UserService userService;
    @Autowired
    TeacherService teacherService;

    @GetMapping("users-teacher")
    public ApiResponse<Page<UsersDto>> getAllTeacher(@RequestParam(required = false) String search,
                                                     @RequestParam(required = false) String status,
                                                     @RequestParam(required = false) Integer gender,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.okStatus(teacherService.findTeacher(search, status, gender, page, size));
    }

    @GetMapping("teacher-classroom")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<TeacherDto>> teacherByClassRoom(
            @RequestParam Integer id) {
        return ApiResponse.okStatus(teacherService.teacherByClassRoom(id));
    }


    @GetMapping("teacher-subject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<TeacherDto>> teacherBySubject(
            @RequestParam Integer id) {
        return ApiResponse.okStatus(teacherService.teacherBySubject(id));
    }
}
