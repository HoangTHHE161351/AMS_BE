package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.semester.request.EditSemesterRequest;
import vn.attendance.service.semester.service.SemesterService;
import vn.attendance.service.semester.response.SemesterDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/semester")
public class SemesterController {

    @Autowired
    SemesterService semesterService;

    @GetMapping("all-semester")
    public ApiResponse<Page<SemesterDto>> searchSemester(@RequestParam(required = false) String search,
                                                         @RequestParam(required = false) String status,
                                                         @RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.okStatus(semesterService.searchSemester(search, status,page, size));
    }

    @PostMapping("add-semester")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> addSemester(@RequestBody @Valid AddSemesterRequest request) throws AmsException {
        semesterService.addSemester(request, 1);
        return ApiResponse.okStatus("Add Semester Success");
    }

    @PostMapping("import-semester")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> importSemester(@RequestBody List<AddSemesterRequest> requests) throws AmsException {
        return ApiResponse.okStatus(semesterService.importSemester(requests));
    }

    @PutMapping("edit-semester")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> updateSemester(@RequestBody EditSemesterRequest request) throws AmsException {
        semesterService.updateSemester(request);
        return ApiResponse.okStatus("Update Semmeter successfully");
    }


    @DeleteMapping("delete-semester")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> deleteSemester(@RequestParam Integer id) throws AmsException {
        semesterService.deleteSemester(id);
        return ApiResponse.okStatus("Delete Semester Success");
    }

    @GetMapping("dropdown-semester")
    ApiResponse<?> getAllSemester() {
        return ApiResponse.okStatus(semesterService.getAllSemester());
    }
}
