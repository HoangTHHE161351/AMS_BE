package vn.attendance.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.attendance.controller.ModuleController;
import vn.attendance.controller.SocketController;
import vn.attendance.exception.AmsException;
import vn.attendance.service.server.service.FaceRecognitionService;
import vn.attendance.util.Constants;

@Component
public class ScheduleTasks {

    @Autowired
    FaceRecognitionService faceRecognitionService;

    @Autowired
    ModuleController moduleController;

    @Scheduled(cron = "0 0 0 * * ?")
    public void UpdateUserToMQTT() throws AmsException, InterruptedException {
        faceRecognitionService.addAllUserTask();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void UpdateUserToServer() throws AmsException, InterruptedException {
        faceRecognitionService.synchronizeFace();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void CallModuleAIUpdate() throws AmsException, InterruptedException {
        moduleController.callModelingApi();
    }

    @Scheduled(cron = "0 20 7 * * ?")
    @Scheduled(cron = "0 50 9 * * ?")
    @Scheduled(cron = "0 40 12 * * ?")
    @Scheduled(cron = "0 10 15 * * ?")
    public void activateRoomEntryTask() throws AmsException {
        System.out.println("Active to White List");
        faceRecognitionService.processRoomEntryTask(Constants.PERSON_TYPE.WHITELIST);
    }

    @Scheduled(cron = "0 00 10 * * ?")
    @Scheduled(cron = "0 30 12 * * ?")
    @Scheduled(cron = "0 20 15 * * ?")
    @Scheduled(cron = "0 50 17 * * ?")
    public void deactivateRoomEntryTask() throws AmsException, InterruptedException {
        System.out.println("Inactive to Black List");
        faceRecognitionService.processRoomEntryTask(Constants.PERSON_TYPE.BLACKLIST);
    }
}
