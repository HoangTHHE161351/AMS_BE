package vn.attendance.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.attendance.exception.AmsException;
import vn.attendance.service.server.response.CameraStateRes;
import vn.attendance.service.server.response.FaceDetectRes;
import vn.attendance.service.server.service.FaceRecognitionService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/socket")
@Slf4j
public class SocketController {
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private FaceRecognitionService faceRecognitionService;

    @Scheduled(fixedRate = 5000)
    public void sendMessage() {
        template.convertAndSend("/topic/greetings1", "Hello");
    }

    @GetMapping
    public void anc(@RequestParam String me){
        template.convertAndSend("/topic/greetings1", me);
    }

    @GetMapping("server-status")
    public void sendServerConnectMessage(@RequestParam String message) {
        template.convertAndSend("/topic/server-status", message);
    }

    @GetMapping("camera-status")
    public void sendCameraStateMessage(@RequestParam CameraStateRes message) {
        template.convertAndSend("/topic/camera-status", message);
    }

    @GetMapping("notify-status")
    public void notifyStatus(@RequestParam String message) {
        template.convertAndSend("/topic/notify-status", message);
    }

    @GetMapping("log-status")
    public void logStatus(@RequestParam String message) {
        template.convertAndSend("/topic/log-status", message);
    }

    @GetMapping("get-image")
    public void getImageMessage(@RequestParam FaceDetectRes message) {
        template.convertAndSend("/topic/get-image", message);
    }

    @MessageMapping("send-result")
    @SendTo("send-result")
    public void handleSendResult(@Payload String message) throws AmsException {
        // Convert message to string and process it
        System.out.println(message);
        log.info("Received message: {}", message);

        CompletableFuture.supplyAsync(() -> {
            try {
                faceRecognitionService.extractModuleLog(message);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return null;
        });
    }


}
