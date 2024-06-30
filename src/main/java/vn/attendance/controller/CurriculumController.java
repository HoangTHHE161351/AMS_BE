package vn.attendance.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.curriculum.request.AddCurriculumRequest;
import vn.attendance.service.curriculum.request.EditCurriculumRequest;
import vn.attendance.service.curriculum.service.CurriculumService;
import vn.attendance.service.curriculumSubject.request.AddCurriculumSubjectRequest;
import vn.attendance.service.curriculumSubject.request.EditCurriculumSubjectRequest;
import vn.attendance.service.curriculumSubject.service.CurriculumSubjectService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/curriculum")
public class CurriculumController {

    @Autowired
    CurriculumService curriculumService;

    @Autowired
    CurriculumSubjectService curriculumSubjectService;

    @GetMapping("all-curriculum")
    public ApiResponse<?> allCurriculum(@RequestParam(required = false) String search,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.okStatus(curriculumService.findCurriculum(search, page,size));
    }

    @GetMapping("curriculum-details")
    public ApiResponse<?> curriculumDetails(@RequestParam int id) throws AmsException {
        return ApiResponse.okStatus(curriculumService.findById(id));
    }

    @PostMapping("add-curriculum")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> addCurriculum(@Valid @RequestBody AddCurriculumRequest addCurriculumRequest) throws AmsException {
        curriculumService.addCurriculum(addCurriculumRequest);
        return ApiResponse.okStatus("Add Success!");
    }

    @PutMapping("edit-curriculum")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> editCurriculum(@PathVariable int id, @Valid @RequestBody EditCurriculumRequest editCurriculumRequest) throws AmsException {
        curriculumService.editCurriculum(id, editCurriculumRequest);
        return ApiResponse.okStatus("Edit Success!");
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
    public ApiResponse<?> getAllCurriculumSub(@RequestParam int curriculumId) {
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

}
