package vn.attendance.service.server.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Users;
import vn.attendance.service.server.response.IFaceDto;

public interface FaceRecognitionService {
    public void SynchronizeFace();
    public Page<IFaceDto> getListFace (String search, String roleName, String status, Integer gender, int page, int size);
    public Users CreateFace(Users user) throws AmsException;
    public Users EditFace(Users users) throws AmsException;
    public boolean DeleteFace(Users user) throws AmsException;
}
