package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.model.Semester;
import vn.attendance.model.Subject;
import vn.attendance.service.curriculum.service.CurriculumService;
import vn.attendance.service.subject.request.AddSubjectRequest;
import vn.attendance.service.subject.request.EditSubjectRequest;
import vn.attendance.service.subject.response.SubjectDto;
import vn.attendance.service.subject.service.SubjectService;
import vn.attendance.service.teacher.service.response.TeacherDto;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;
    @Autowired
    CurriculumService curriculumService;
    @GetMapping("subject")
    public ApiResponse<Page<Subject>> getAllSubject(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int size){
        Page<Subject> subjectPage = subjectService.findAllSubject(page,size);
        return ApiResponse.okStatus(subjectPage);
    }
    
    @PostMapping("add-subject")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> addSubject(@RequestBody @Valid AddSubjectRequest request) throws AmsException {
        subjectService.addSubject(request);
        return ApiResponse.okStatus("Add Subject Success!");
    }

    @PutMapping("edit-subject")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ApiResponse<?> updateSubject(@PathVariable Integer id,@Valid @RequestBody EditSubjectRequest subjectDetails) {
        Optional<Subject> updatedSubject = subjectService.updateSubject(id, subjectDetails);
        if (updatedSubject.isPresent()) {
            return ApiResponse.okStatus(updatedSubject.get());
        } else {
            return ApiResponse.errorStatus("Subject not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("dropdown-subject")
    public ApiResponse<?> getAllSubjects() throws Exception {
        return ApiResponse.okStatus(subjectService.findAllSubject());
    }


    @GetMapping("search-subject")

    public ApiResponse<Page<SubjectDto>> searchSubject(@RequestParam(required = false) String search,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size){
        return ApiResponse.okStatus(subjectService.searchSubject(search,  page, size));
    }

    @DeleteMapping("delete-subject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> deleteSubject(@RequestParam Integer id) throws AmsException {
        Optional<Subject> deletedSubject = subjectService.deleteSubject(id);
        if (deletedSubject.isPresent())
            return ApiResponse.okStatus(deletedSubject.get());
        else return ApiResponse.errorStatus("Not found!");
    }

}
