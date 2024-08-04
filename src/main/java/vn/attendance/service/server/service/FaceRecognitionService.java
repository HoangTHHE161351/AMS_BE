package vn.attendance.service.server.service;

import vn.attendance.exception.AmsException;
import vn.attendance.job.entity.ChangeRequest;
import vn.attendance.model.Users;
import vn.attendance.service.server.request.SendImageRequest;
import vn.attendance.service.server.response.FaceDetectRes;

public interface FaceRecognitionService {
    Users createFace(Users user) throws AmsException;
    Users editFace(Users users) throws AmsException;
    boolean deleteFace(Users user) throws AmsException;

    void extractModuleLog(String message) throws AmsException;
    void extractJsonLcdLog(String message);
    void sendImageToModule(SendImageRequest sendImageRequest);
    void getResultCompare(FaceDetectRes res);

    void updateAllFace() throws AmsException, InterruptedException;
    void addAllUserTask() throws AmsException, InterruptedException;
    void synchronizeFace() throws AmsException;

    void processRoomEntryTask(Integer personType) throws AmsException;
    void sendChangeStateRequest(ChangeRequest request) throws AmsException;
    void hotAddUserRequest(Integer userId) throws AmsException;


}
