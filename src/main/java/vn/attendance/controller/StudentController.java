package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.student.request.AddStudentCurriculum;
import vn.attendance.service.student.request.EditStudentCurriculum;
import vn.attendance.service.student.response.StudentCurriculumDto;
import vn.attendance.service.student.service.StudentService;
import vn.attendance.service.user.service.UserService;
import vn.attendance.service.user.service.response.UsersDto;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/student")
public class StudentController {
    @Autowired
    UserService userService;

    @Autowired
    StudentService studentService;


    @GetMapping("search-student")
    public ApiResponse<Page<UsersDto>> searchStudent(@RequestParam(required = false) String search,
                                                     @RequestParam(required = false) String status,
                                                     @RequestParam(required = false) String curriculumName,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.okStatus(studentService.searchStudent(search, curriculumName, status, page, size));
    }

    @GetMapping("export-student")
    public ResponseEntity<byte[]> exportStudent(@RequestParam(required = false) String search,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(required = false) String curriculumName) throws AmsException {
        byte[] bytes = studentService.exportStudent(search, status, curriculumName);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student.xls");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes.length));

        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @GetMapping("student-profile")
    public ApiResponse<?> findStudentById(@RequestParam Integer id) {
        return ApiResponse.okStatus(studentService.getStudentById(id));
    }

    @PostMapping("add-student-curriculum")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> addStudentCurriculum(@Valid @RequestBody AddStudentCurriculum request) throws AmsException {
        return ApiResponse.okStatus(studentService.addStudentCurriculum(request));
    }

    @PutMapping("edit-student-curriculum")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> updateStudentCurriculum(@RequestParam Integer id, @RequestBody EditStudentCurriculum request) throws AmsException {
        return ApiResponse.okStatus(studentService.updateStudentCurriculum(id,request));
    }

    @DeleteMapping("delete-student-curriculum")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> deleteStudentCurriculum(@RequestParam Integer id) throws AmsException {
        studentService.deleteStudentCurriculum(id);
        return ApiResponse.okStatus("Delete Student Success");
    }

    @GetMapping("search-student-curriculum")
    public ApiResponse<?> searchStudentCurriculum(@RequestParam(required = false) String search,
                                                                           @RequestParam(defaultValue = "1") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.okStatus(studentService.searchStudentCurriculum(search,page, size));
    }

    @GetMapping("dropdown-student")
    public ApiResponse<?> dropdownStudent(@RequestParam(required = false) String search) {
        return ApiResponse.okStatus(studentService.dropdownStudent(search));
    }
}
