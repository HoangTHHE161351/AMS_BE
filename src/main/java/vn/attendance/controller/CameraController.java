package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.camera.request.AddCameraRequest;
import vn.attendance.service.camera.request.EditCameraRequest;
import vn.attendance.service.camera.service.CameraService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/camera")
public class CameraController {
    @Autowired
    CameraService cameraService;

    @GetMapping("camera")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> getAllCamera(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(required = false) String search,
                                       @RequestParam(required = false)  String status,
                                       @RequestParam(required = false) Integer roomId
                                       ){
        return ApiResponse.okStatus(cameraService.findAllCamera(search, status, roomId, page,size));
    }

    @PostMapping("add-camera")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> addCamera(@RequestBody @Valid AddCameraRequest request) throws AmsException {
        return ApiResponse.okStatus(cameraService.addCamera(request, 1));
    }

    @PutMapping("edit-camera")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> updateCamera(@RequestBody @Valid EditCameraRequest request) throws AmsException {
        cameraService.editCamera(request);
        return ApiResponse.okStatus("Update camera Success!");
    }

    @DeleteMapping("delete-camera")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> deleteCamera(@RequestParam Integer cameraId) throws AmsException {
        cameraService.deleteCamera(cameraId);
        return ApiResponse.okStatus("Delete camera Success!");
    }

    @PostMapping("import-camera")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> importCamera(@RequestBody List<AddCameraRequest> cameraList) throws AmsException {
        return ApiResponse.okStatus(cameraService.importCamera(cameraList));
    }

    @GetMapping("get-camera-access")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> getCameraAccess(@RequestParam Integer cameraId) throws AmsException {
        cameraService.getCameraAccess(cameraId);
        return ApiResponse.okStatus("Get Access Success");
    }

    @GetMapping("get-camera-access-by-room")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ApiResponse<?> getCameraAccessByRoom(@RequestParam Integer roomId) throws AmsException {
        cameraService.getCameraAccessByRoom(roomId);
        return ApiResponse.okStatus("Get Access Success");
    }

}
