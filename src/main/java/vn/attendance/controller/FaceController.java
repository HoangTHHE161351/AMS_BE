package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.facial.request.AddFacialRequest;
import vn.attendance.service.facial.request.SetFaceRequest;
import vn.attendance.service.facial.service.FacialService;
import vn.attendance.service.server.service.LogAccessService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("api/v1/face")
public class FaceController {

    @Autowired
    FacialService facialService;
    @Autowired
    LogAccessService logAccessService;

    @GetMapping("view-faceId")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> getFaceIdByUser(@RequestParam int userId) throws AmsException {
        return ApiResponse.okStatus(facialService.getFacialByUserId(userId));
    }

    @PostMapping("add-faceId")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> addFaceId(@RequestParam int userId,
                                    @RequestBody AddFacialRequest request) throws AmsException {
        facialService.addFacialData(request, userId);
        return ApiResponse.okStatus("Add Face Image Successfully");
    }

    @PostMapping("set-faceId")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> setFace(@RequestBody SetFaceRequest request) throws AmsException {
        facialService.setFacialData(request);
        return ApiResponse.okStatus("Set Face Image Successfully");
    }

    @DeleteMapping("delete-faceId")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> deleteFaceId(@RequestParam int userId,
                                       @RequestBody List<Integer> requestlist) throws AmsException {
        return ApiResponse.okStatus(facialService.deleteFacialData(requestlist, userId));
    }

    @GetMapping("get-image-logs")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ApiResponse<?> getImageLog(@RequestParam String time) throws AmsException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(time, formatter);
        } catch (DateTimeParseException e) {
            throw new AmsException("Invalid time format");
        }
        return ApiResponse.okStatus(logAccessService.getImageLog(localDateTime));
    }
}
