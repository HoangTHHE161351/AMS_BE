package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.model.Camera;
import vn.attendance.service.camera.request.AddCameraRequest;
import vn.attendance.service.camera.service.CameraService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/camera")
public class CameraController {
    @Autowired
    CameraService cameraService;
    @GetMapping("camera")
    public ApiResponse<Page<Camera>> getAllCamera(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size){
        Page<Camera> cameraPage = cameraService.findAllCamera(page,size);
        return ApiResponse.okStatus(cameraPage);
    }

    @PostMapping("add-camera")
    public ApiResponse<?> addCamera(@RequestBody @Valid AddCameraRequest request) throws AmsException {
        cameraService.addCamera(request);
        return ApiResponse.okStatus("Add camera Success!");
    }

    @PostMapping("edit-camera")
    public ApiResponse<?> updateCamera(@RequestBody @Valid AddCameraRequest request) throws AmsException {
        cameraService.editCamera(request);
        return ApiResponse.okStatus("Update camera Success!");
    }

    @GetMapping("delete-camera")
    public ApiResponse<?> deleteCamera(@RequestParam Integer cameraId) throws AmsException {
        cameraService.deleteCamera(cameraId);
        return ApiResponse.okStatus("Delete camera Success!");
    }

}
