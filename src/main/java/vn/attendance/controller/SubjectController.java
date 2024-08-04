package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.curriculum.service.CurriculumService;
import vn.attendance.service.subject.request.AddSubjectRequest;
import vn.attendance.service.subject.request.EditSubjectRequest;
import vn.attendance.service.subject.service.SubjectService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;
    @Autowired
    CurriculumService curriculumService;

    @PostMapping("add-subject")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> addSubject(@RequestBody @Valid AddSubjectRequest request) throws AmsException {
        subjectService.addSubject(request, 1);
        return ApiResponse.okStatus("Add Subject Successfully!");
    }

    @PostMapping("import-subject")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> importSubject(@RequestBody List<AddSubjectRequest> requests) throws AmsException {
        return ApiResponse.okStatus(subjectService.importSubject(requests));
    }

    @PutMapping("edit-subject")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> updateSubject(@Valid @RequestBody EditSubjectRequest subjectDetails) throws AmsException {
        subjectService.updateSubject(subjectDetails);
        return ApiResponse.okStatus("Update Subject Success!");
    }

    @GetMapping("dropdown-subject")
    public ApiResponse<?> getDropdownSubject() {
        return ApiResponse.okStatus(subjectService.getDropdownSubject());
    }

    @DeleteMapping("delete-subject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> deleteSubject(@RequestParam Integer id) throws AmsException {
        subjectService.deleteSubject(id);
        return ApiResponse.okStatus("Delete subject successfully");
    }

    @GetMapping("all-subjects")
    public ApiResponse<?> searchSubject(@RequestParam(required = false) String search,
                                        @RequestParam(required = false) String status,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return ApiResponse.okStatus(subjectService.findSubject(search, status, page, size));
    }


}
