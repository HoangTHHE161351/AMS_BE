package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.attendance.exception.AmsException;
import vn.attendance.helper.ApiResponse;
import vn.attendance.job.entity.ChangeRequest;
import vn.attendance.service.server.request.SendImageRequest;
import vn.attendance.service.server.response.FaceDetectRes;
import vn.attendance.service.server.service.FaceRecognitionService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/face-recognition")
public class FaceRecognitionController {
    @Autowired
    FaceRecognitionService faceRecognitionService;

    @GetMapping("update-all-face")
    public ApiResponse<?> updateAllFace() throws AmsException, InterruptedException {
        faceRecognitionService.updateAllFace();
        return ApiResponse.okStatus("Send Request Update All Face Success");
    }

    @GetMapping("active-entry")
    public ApiResponse<?> activateRoomEntryTask(@RequestParam Integer personType) throws AmsException {
        faceRecognitionService.processRoomEntryTask(personType);
        return ApiResponse.okStatus("Send Entry Task Complete");
    }

    @PostMapping("/send-change-state")
    public ApiResponse<?> sendChangeStateRequest(@RequestBody ChangeRequest request) throws AmsException, InterruptedException {
        faceRecognitionService.sendChangeStateRequest(request);
        return ApiResponse.okStatus("Send Request Success");
    }

    @GetMapping("hot-update")
    public ApiResponse<?> hotAddUserRequest(@RequestParam Integer userId) throws AmsException {
        faceRecognitionService.hotAddUserRequest(userId);
        return ApiResponse.okStatus("Hot Update Request Success!");
    }

    @PostMapping("get-json-cctv")
    public ApiResponse<?> getJsonCctv(@RequestBody String mess) throws AmsException {
        faceRecognitionService.extractModuleLog(mess);
        return ApiResponse.okStatus("Ok CHECK");
    }

    @PostMapping("send-image-module")
    public ApiResponse<?> sendImageModule(@RequestBody SendImageRequest request) {
        request.setTime(LocalDateTime.now());
        faceRecognitionService.sendImageToModule(request);
        return ApiResponse.okStatus("Ok Send");
    }

    @PostMapping("get-result-compare")
    public ApiResponse<?> getResultCompate(@RequestBody FaceDetectRes request) {
        faceRecognitionService.getResultCompare(request);
        return ApiResponse.okStatus("Ok Send");
    }

}
