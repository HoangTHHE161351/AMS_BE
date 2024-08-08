package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.teacher.request.AddTeacherSubjectRequest;
import vn.attendance.service.teacher.request.EditTeacherSubjectRequest;
import vn.attendance.service.teacher.response.TeacherDto;
import vn.attendance.service.teacher.service.TeacherService;
import vn.attendance.service.teacherSubject.service.TeacherSubjectService;
import vn.attendance.service.user.service.UserService;
import vn.attendance.service.user.service.response.UsersDto;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/teacher")
public class TeacherController {

    @Autowired
    UserService userService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    TeacherSubjectService teacherSubjectService;

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

    @GetMapping("dropdown-teacher")
    public ApiResponse<?> getTeacherDropList(@RequestParam Integer subjectId) {
        return ApiResponse.okStatus(teacherService.getTeacherDropList(subjectId));
    }

    @GetMapping("dropdown-teacher-for-schedule")
    public ApiResponse<?> getTeacherForSchedule(@RequestParam Integer subjectId, LocalDate date, Integer slotId) {
        return ApiResponse.okStatus(teacherService.getTeacherForSchedule(subjectId, date, slotId));
    }


    @GetMapping("teacher-subject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> teacherBySubject(@RequestParam Integer id) throws AmsException {
        return ApiResponse.okStatus(teacherSubjectService.findAllTeacherSubject(id));
    }

    @PostMapping("add-teacher-subject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> addTeacherSubject(@RequestBody AddTeacherSubjectRequest request) throws AmsException {
        teacherSubjectService.addTeacherSubject(request, 1);
        return ApiResponse.okStatus("Add Subject for Teacher Success");
    }

    @PostMapping("import-teacher-subject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> importTeacherSubject(@RequestBody List<AddTeacherSubjectRequest> requests) throws AmsException {
        return ApiResponse.okStatus(teacherSubjectService.importTeacherSubject(requests));
    }

    @DeleteMapping("delete-teacher-subject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> deleteTeacherSubject(@RequestParam Integer id) throws AmsException {
        teacherService.deleteTeacherSubject(id);
        return ApiResponse.okStatus("Delete Teacher Subject Success");
    }

    @PutMapping("edit-teacher-subject/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> updateTeacherSubject(@PathVariable Integer id, @Valid @RequestBody EditTeacherSubjectRequest teachersubjectDetails) throws AmsException {
        teacherService.updateTeacherSubject(id, teachersubjectDetails);
        return ApiResponse.okStatus("Update Teacher Subject Success");

    }
}
