package vn.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Notify;
import vn.attendance.repository.UsersRepository;
import vn.attendance.service.notify.service.NotifyService;
import vn.attendance.service.server.response.FaceDetectRes;
import vn.attendance.service.server.service.FaceRecognitionService;
import vn.attendance.util.Constants;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/module")
public class ModuleController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FaceRecognitionService faceRecognitionService;

    String url = "http://localhost:9191/";

    @GetMapping("call-modeling-api")
    @Scheduled(cron = "0 0 0 * * ?")
    public ResponseEntity<?> callModelingApi() throws AmsException {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url + "add_person", String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                faceRecognitionService.addNotifyForSynchronize(Constants.NOTIFY_TITLE.SYNCHRONIZE_SUCCESS, "AI module successfully analyzes entire face");
                return ResponseEntity.ok(response.getBody());
            } else {
                // Trường hợp mã trạng thái không phải 2xx
                throw new Exception();
            }

        } catch (Exception e) {
            // Trường hợp xảy ra ngoại lệ
            faceRecognitionService.addNotifyForSynchronize(Constants.NOTIFY_TITLE.SYNCHRONIZE_FAIL, "AI module fails to analyze entire face");
            return ResponseEntity.status(500).body("Error call: Module AI not Connect");
        }
    }

/*    @PostMapping("get-face-compare")
    public String getFaceCompare(@RequestBody FaceDetectRes faceDetectRes) {
        try {
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create HttpEntity
            HttpEntity<FaceDetectRes> requestEntity = new HttpEntity<>(faceDetectRes, headers);

            // Send POST request
            ResponseEntity<String> response = restTemplate.postForEntity(url + "process_image", requestEntity, String.class);
            return response.getBody();
//            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            System.out.println("Error calling FastAPI: " + e.getMessage());
            return null;

//            return ResponseEntity.status(500).body("Error calling FastAPI: " + e.getMessage());

        }
    }*/
}
