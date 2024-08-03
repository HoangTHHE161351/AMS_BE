package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.curriculum.request.AddCurriculumRequest;
import vn.attendance.service.curriculum.request.AddStudentCurriculumRequest;
import vn.attendance.service.curriculum.request.AddSubjectCurriculumRequest;
import vn.attendance.service.curriculum.request.EditCurriculumRequest;
import vn.attendance.service.curriculum.service.CurriculumService;
import vn.attendance.service.curriculumSubject.request.AddCurriculumSubjectRequest;
import vn.attendance.service.curriculumSubject.request.EditCurriculumSubjectRequest;
import vn.attendance.service.curriculumSubject.service.CurriculumSubjectService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/curriculum")
public class CurriculumController {

    @Autowired
    CurriculumService curriculumService;

    @Autowired
    CurriculumSubjectService curriculumSubjectService;

    @GetMapping("all-curriculum")
    public ApiResponse<?> allCurriculum(@RequestParam(required = false) String search,
                                        @RequestParam(required = false) String status,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.okStatus(curriculumService.findCurriculum(search, status, page, size));
    }

    @GetMapping("curriculum-details")
    public ApiResponse<?> curriculumDetails(@RequestParam int id) throws AmsException {
        return ApiResponse.okStatus(curriculumService.findById(id));
    }

    @PostMapping("add-curriculum")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> addCurriculum(@Valid @RequestBody AddCurriculumRequest request) throws AmsException {
        curriculumService.addCurriculum(request, 1);
        return ApiResponse.okStatus("Add Success!");
    }

    @PostMapping("import-curriculum")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> importCurriculum(@RequestBody List<AddCurriculumRequest> requests) throws AmsException {
        return ApiResponse.okStatus(curriculumService.importCurriculum(requests));
    }

    @PutMapping("edit-curriculum")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> editCurriculum(@Valid @RequestBody EditCurriculumRequest editCurriculumRequest) throws AmsException {
        curriculumService.editCurriculum(editCurriculumRequest);
        return ApiResponse.okStatus("Edit Curriculum Success!");
    }

    @DeleteMapping("delete-curriculum")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> deleteCurriculum(@RequestParam int id) throws AmsException {
        curriculumService.deleteCurriculum(id);
        return ApiResponse.okStatus("Delete Success!");
    }

    @GetMapping("dropdown-curriculum")
    public ApiResponse<?> getAll() throws AmsException {
        return ApiResponse.okStatus(curriculumService.findAllCurriculum());
    }


    @GetMapping("find-curriculum-sub")
    public ApiResponse<?> getAllCurriculumSub(@RequestParam int curriculumId) throws AmsException {
        return ApiResponse.okStatus(curriculumSubjectService.findAllByCurriculumId(curriculumId));
    }

    @PostMapping("/add-curriculum-sub")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> addCurriculumSub(@Valid @RequestBody AddCurriculumSubjectRequest request) throws AmsException {
        curriculumSubjectService.addCurriculumSubject(request);
        return ApiResponse.okStatus("Add CurriculumSub successfully!");
    }

    @PostMapping("/edit-curriculum-sub")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> editCurriculumSub(Integer id, @Valid @RequestBody EditCurriculumSubjectRequest request) throws AmsException {
        curriculumSubjectService.editCurriculumSubject(id, request);
        return ApiResponse.okStatus("Edit CurriculumSub successfully!");
    }

    @DeleteMapping("delete-curriculum-sub")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> deleteCurriculumSub(Integer id) throws AmsException {
        curriculumSubjectService.deleteCurriculumSub(id);
        return ApiResponse.okStatus("Delete CurriculumSub successfully!");
    }

    @GetMapping("all-curriculum-subject")
    public ApiResponse<?> allCurriculumSubject(@RequestParam(required = false) String search,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.okStatus(curriculumSubjectService.findCurriculumSubject(search, page, size));
    }

    @PostMapping("import-student-curriculum")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> importStudentCurriculum(@RequestBody List<AddStudentCurriculumRequest> request) throws AmsException {
        return ApiResponse.okStatus(curriculumService.importStudentCurriculum(request));
    }

    @PostMapping("add-student-curriculum")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> addStudentCurriculum(@RequestBody AddStudentCurriculumRequest request) throws AmsException {
        curriculumService.addStudentCurriculum(request, 1);
        return ApiResponse.okStatus("Add Curriculum for Student Success");
    }

    @PostMapping("import-subject-curriculum")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> importSubjectCurriculum(@RequestBody List<AddSubjectCurriculumRequest> request) throws AmsException {
        return ApiResponse.okStatus(curriculumService.importSubjectCurriculum(request));
    }

    @PostMapping("add-subject-curriculum")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> addSubjectCurriculum(@RequestBody AddSubjectCurriculumRequest request) throws AmsException {
        curriculumService.addSubjectCurriculum(request, 1);
        return ApiResponse.okStatus("Add Subject for Curriculum Success");
    }

    @GetMapping("get-all-subjects-curiculum")
    public ApiResponse<?> getAllSubjectsCurriculum(@RequestParam Integer curriculumId ) throws AmsException {

        return ApiResponse.okStatus(curriculumService.getAllSubjectsCurriculum(curriculumId));
    }

    @GetMapping("get-all-curriculums-student")
    public ApiResponse<?> getAllCurriculumsStudent(@RequestParam Integer studentId ) throws AmsException {

        return ApiResponse.okStatus(curriculumService.getAllCurriculumsStudent(studentId));
    }
}
