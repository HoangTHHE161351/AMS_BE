package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.model.Semester;
import vn.attendance.model.Users;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.semester.request.EditSemesterRequest;
import vn.attendance.service.semester.service.SemesterService;
import vn.attendance.service.semester.service.response.SemesterDto;
import vn.attendance.service.subject.request.AddSubjectRequest;
import vn.attendance.service.teacher.service.TeacherService;
import vn.attendance.service.user.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/semester")
public class SemesterController {

    @Autowired
    SemesterService semesterService;

    @GetMapping("all-semester")
    public ApiResponse<Page<SemesterDto>> searchSemester(@RequestParam(required = false) String search,
                                                         @RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int size){
        return ApiResponse.okStatus(semesterService.searchSemester(search,  page, size));
    }

    @PostMapping("add-semester")

    public ApiResponse<?> addSemester(@RequestBody @Valid AddSemesterRequest request) throws AmsException {
        return ApiResponse.okStatus(semesterService.addSemester(request));
    }

    @PutMapping("edit-semester")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> updateSemester(@RequestParam Integer id, @RequestBody EditSemesterRequest request) throws AmsException {
        return ApiResponse.okStatus(semesterService.updateSemester(id,request));
    }



    @DeleteMapping("delete-semester")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> deleteSemester(@RequestParam Integer id) throws AmsException {
        Optional<Semester> deletedSemester = semesterService.deleteSemester(id);
        if (deletedSemester.isPresent())
            return ApiResponse.okStatus(deletedSemester.get());
        else return ApiResponse.errorStatus("Not found!");
    }
    @GetMapping("dropdown-semester")
    ApiResponse<?> getAllSemester(){
        return ApiResponse.okStatus(semesterService.getAllSemester());
    }
}
