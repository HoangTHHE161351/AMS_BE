package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.cameraRoom.request.AddCameraRoomRequest;
import vn.attendance.service.cameraRoom.service.impl.CameraRoomServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/cameraroom")
public class CameraRoomController {
    @Autowired
    private CameraRoomServiceImpl cameraRoomService;

    @GetMapping("all-cameraroom")
    public ApiResponse<?> getAllCameraRoom(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return ApiResponse.okStatus(cameraRoomService.findAllCameraRoom(page, size));
    }

    @PostMapping("add-cameraroom")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> addCameraRoom(@RequestBody @Valid AddCameraRoomRequest request) throws AmsException {
        cameraRoomService.addCameraRoom(request);
        return ApiResponse.okStatus("Add cameraroom successfully");
    }

    @PostMapping("edit-cameraroom")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> editCameraRoom(@RequestParam Integer id, @RequestBody @Valid AddCameraRoomRequest request) throws AmsException {
        cameraRoomService.editCameraRoom(id, request);
        return ApiResponse.okStatus("Edit cameraRoom successfully");
    }

    @DeleteMapping("delete-cameraroom")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> changeCameraRoomStatus(@RequestParam Integer id) throws AmsException {
        cameraRoomService.deleteCamera(id);
        return ApiResponse.okStatus("Delete CameraRoom successfully");
    }
}
