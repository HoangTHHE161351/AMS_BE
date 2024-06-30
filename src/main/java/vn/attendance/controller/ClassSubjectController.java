package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
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






}
