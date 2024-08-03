package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.classRoom.request.AddClassRoomRequest;
import vn.attendance.service.classRoom.request.EditClassRoomRequest;
import vn.attendance.service.classRoom.service.ClassRoomService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/classroom")
public class ClassRoomController {

    @Autowired
    ClassRoomService classRoomService;

    @GetMapping("all-classroom")
    public ApiResponse<?> searchClassRoom(@RequestParam(required = false) String search,
                                          @RequestParam(required = false) String status,
                                          @RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        return ApiResponse.okStatus(classRoomService.findAllClassRoom(search, status, page, size));
    }

    @GetMapping("dropdown-classroom")
    public ApiResponse<?> searchClassRoom(@RequestParam(required = false) Integer subjectId) {
        return ApiResponse.okStatus(classRoomService.findAllClassRoom(subjectId));
    }

    @GetMapping("classroomDetail")
    public ApiResponse<?> findClassRoom(@RequestParam Integer id) throws AmsException {
        return ApiResponse.okStatus(classRoomService.findById(id));
    }

    @PostMapping("add-classroom")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> addClassRoom(@RequestBody @Valid AddClassRoomRequest request) throws AmsException {
        classRoomService.addClassRoom(request, 1);
        return ApiResponse.okStatus("Add Classroom Success!");
    }

    @PostMapping("edit-classroom")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> editclassRoom(@RequestParam Integer id, @RequestBody @Valid EditClassRoomRequest request) throws AmsException {
        classRoomService.editClassRoom(id, request);
        return ApiResponse.okStatus("Edit Classroom successfully");
    }

    // Handle Delete
    @DeleteMapping("delete-classroom")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> changeClassRoomStatus(@RequestParam Integer id) throws AmsException {
        classRoomService.deleteClassRoom(id);
        return ApiResponse.okStatus("Delete Classroom Successfully");
    }

    @GetMapping("dropdown-class")
    public ApiResponse<?> getAllClasses() throws AmsException {
        return ApiResponse.okStatus(classRoomService.findAll());
    }


    @GetMapping("export-classroom")
    public ResponseEntity<byte[]> exportClassRoomExcel(@RequestParam(required = false) String search) throws AmsException {
        byte[] bytes = classRoomService.exportClassRoom(search);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=classrooms.xls");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes.length));

        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @PostMapping("import-classroom")
    public ApiResponse<List<AddClassRoomRequest>> importUser(@RequestBody List<AddClassRoomRequest> classRoomRequests) throws AmsException {
        return ApiResponse.okStatus(classRoomService.importClassRoom(classRoomRequests));
    }


}
