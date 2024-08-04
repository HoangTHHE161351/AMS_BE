package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.classSubject.request.AddClassSubjectRequest;
import vn.attendance.service.classSubject.request.EditClassSubjectRequest;
import vn.attendance.service.classSubject.service.ClassSubjectService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/class-subject")
public class ClassSubjectController {


    @Autowired
   ClassSubjectService classSubjectService;
    @GetMapping("all-class-subject")
    public ApiResponse<?> allClassSubject(@RequestParam(required = false) String search,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.okStatus(classSubjectService.findClassSubject(search, page,size));
    }

    @PutMapping("edit-class-subject")
    public ApiResponse<EditClassSubjectRequest> editClassSubject(@RequestParam int id, @Valid @RequestBody EditClassSubjectRequest editClassSubjectRequest) throws AmsException {
        EditClassSubjectRequest response = classSubjectService.editClassSubject(id, editClassSubjectRequest);
        return ApiResponse.okStatus(response);
    }


    @PostMapping("/add-class-subject")
    public ApiResponse<AddClassSubjectRequest> addClassSubject(@Valid @RequestBody AddClassSubjectRequest addClassSubjectRequest) throws AmsException {
        AddClassSubjectRequest response = classSubjectService.addClassSubject(addClassSubjectRequest);
        return ApiResponse.okStatus(response);
    }
    @GetMapping("class-subject-details")
    public ApiResponse<?> classSubjectDetails(@RequestParam Integer id) throws AmsException {
        return ApiResponse.okStatus(classSubjectService.findById(id));
    }
    @DeleteMapping("/delete-class-subject")
    public ApiResponse<?> deleteClassSubject(@RequestParam Integer id) throws AmsException {
        classSubjectService.deleteClassSubject(id);
        return ApiResponse.okStatus("Delete Success!");
    }

    @GetMapping("get-classes-subject")
    public ApiResponse<?> getClassesSubject(@RequestParam Integer subjectId) throws AmsException {
        return ApiResponse.okStatus(classSubjectService.getClassesSubject(subjectId));
    }
}
