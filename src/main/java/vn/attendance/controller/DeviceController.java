package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.server.request.ServerLoginRequest;
import vn.attendance.service.server.service.DeviceService;
import vn.attendance.service.server.service.ServerService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/device")
public class DeviceController {
    @Autowired
    DeviceService deviceService;

    @Autowired
    ServerService serverService;

    @PostMapping("server-login")
    public ApiResponse<?> serverLogin(@RequestBody @Valid ServerLoginRequest request) throws AmsException {
        serverService.login(request);
        return ApiResponse.okStatus("Login Successfully");
    }

    @GetMapping("server-logout")
    public ApiResponse<?> serverLogout() throws AmsException {
        serverService.logout();
        return ApiResponse.okStatus("Logout Successfully");
    }

    @GetMapping("get-disk-status")
    public ApiResponse<?> getDiskStatus() throws AmsException {
        return ApiResponse.okStatus(deviceService.GetHardDiskState());
    }

    @GetMapping("get-camera-rule")
    public ApiResponse<?> getCameraRule(@RequestParam Integer cameraId) throws AmsException{
        return ApiResponse.okStatus(deviceService.GetCameraRule(cameraId));
    }
}
