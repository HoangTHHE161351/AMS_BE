package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.attendance.helper.ApiResponse;
import vn.attendance.service.server.service.FaceRecognitionService;

@RestController
@RequestMapping("api/v1/face")
public class FaceController {

    @Autowired
    FaceRecognitionService faceRecognitionService;

    @GetMapping("getFaces")
    public ApiResponse<?> getFaces(@RequestParam(required = false) String search,
                                   @RequestParam(required = false) String status,
                                   @RequestParam(required = false) String roleName,
                                   @RequestParam(required = false) Integer gender,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size){
        return ApiResponse.okStatus(faceRecognitionService.getListFace(search,roleName,status,gender,page,size));
    }
}
