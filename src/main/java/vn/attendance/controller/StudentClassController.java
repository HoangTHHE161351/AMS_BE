package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.studentClass.request.AddStudentClassRequest;
import vn.attendance.service.studentClass.request.EditStudentClassRequest;
import vn.attendance.service.studentClass.service.StudentClassService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student-class")
public class StudentClassController {

    @Autowired
    StudentClassService studentClassService;

    @GetMapping("/all-student-class")
    public ApiResponse<?> allStudentClass(@RequestParam(required = false) String search,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.okStatus(studentClassService.findStudentClass(search, page, size));
    }

    @GetMapping("student-class-details")
    public ApiResponse<?> studentClassDetails(@RequestParam int id) throws AmsException {
        return ApiResponse.okStatus(studentClassService.findById(id));
    }

    @PostMapping("add-student-class")
    public ApiResponse<?> addStudentClass(@Valid @RequestBody AddStudentClassRequest addStudentClassRequest) throws AmsException {
        studentClassService.addStudentClass(addStudentClassRequest, 1);
        return ApiResponse.okStatus("Add Student to Class Success");
    }

    @PostMapping("import-student-class")
    public ApiResponse<?> importStudentClass(@RequestBody List<AddStudentClassRequest> addStudentClassRequest) throws AmsException {
        return ApiResponse.okStatus(studentClassService.importStudentClass(addStudentClassRequest));
    }

    @PutMapping("edit-student-class")
    public ApiResponse<?> editStudentClass(@RequestParam int id, @Valid @RequestBody EditStudentClassRequest editStudentClassRequest) throws AmsException {
        EditStudentClassRequest response = studentClassService.editStudentClass(id, editStudentClassRequest);
        return ApiResponse.okStatus(response);
    }

    @DeleteMapping("delete-student-class")
    public ApiResponse<?> deleteStudentClass(@RequestParam int id) throws AmsException {
        studentClassService.deleteStudentClass(id);
        return ApiResponse.okStatus("Delete Success!");
    }

    @GetMapping("get-all-students-class")
    public ApiResponse<?> getAllStudentsClass(@RequestParam Integer classId) throws AmsException {

        return ApiResponse.okStatus(studentClassService.getAllStudentsClass(classId));
    }
}