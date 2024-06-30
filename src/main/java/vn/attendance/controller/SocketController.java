package vn.attendance.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.attendance.service.server.response.CameraStateRes;
import vn.attendance.service.server.response.FaceDetectRes;

@RestController
@RequestMapping("api/v1/socket")
@Slf4j
public class SocketController {
    @Autowired
    private SimpMessagingTemplate template;

//    @Scheduled(fixedRate = 5000)
//    public void sendMessage() {
//        HelloMessage message = new HelloMessage();
//        log.info("===============SEND MESSAGE BATCH");
//        message.setName("12123: " + LocalDateTime.now());
//        Greeting greeting = new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//        template.convertAndSend("/topic/greetings1", greeting.getContent());
//    }

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

    @GetMapping("get-image")
    public void getImageMessage(@RequestParam FaceDetectRes message) {
        template.convertAndSend("/topic/get-image", message);
    }
}
