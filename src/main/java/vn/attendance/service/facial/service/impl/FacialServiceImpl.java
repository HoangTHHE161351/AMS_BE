package vn.attendance.service.facial.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.controller.ModuleController;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Facial;
import vn.attendance.model.Users;
import vn.attendance.repository.FacialRepository;
import vn.attendance.repository.UsersRepository;
import vn.attendance.service.facial.request.AddFacialRequest;
import vn.attendance.service.facial.request.SetFaceRequest;
import vn.attendance.service.facial.response.FacialResponseDto;
import vn.attendance.service.facial.service.FacialService;
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.service.FaceRecognitionService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FacialServiceImpl implements FacialService {

    @Autowired
    FacialRepository facialRepository;

    @Autowired
    ServerInstance serverInstance;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    FaceRecognitionService faceRecognitionService;

    @Autowired
    ModuleController moduleController;

    @Override
    public List<FacialResponseDto> getFacialByUserId(Integer userId) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
//        if (users.getRoleId() != 1 && users.getRoleId() != 2) {
//            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
//        }

        return facialRepository.findAllFacialByUserId(userId);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public AddFacialRequest addFacialData(AddFacialRequest request, Integer userId) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
//        if (users.getRoleId() != 1 && users.getRoleId() != 2) {
//            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
//        }

        List<Facial> faceList = facialRepository.findFacialByUserId(userId);

        for (Facial facial : faceList) {
            if (facial.getStatus().equals(Constants.STATUS_TYPE.ACTIVE)) {
                facial.setStatus(Constants.STATUS_TYPE.DELETED);
                facial.setModifiedAt(LocalDateTime.now());
                facial.setModifiedBy(users.getId());
                facialRepository.save(facial);
            }
        }

        Facial facial = new Facial();
        facial.setUserId(userId);
        facial.setImage(request.getImage());
        facial.setStatus(Constants.STATUS_TYPE.ACTIVE);
        facial.setCreatedAt(LocalDateTime.now());
        facial.setCreatedBy(users.getId());
        request.setStatus("SUCCESS");
        facialRepository.save(facial);

        return request;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String deleteFacialData(List<Integer> requestList, Integer userId) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();

        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
/*        if (users.getRoleId() != 1 && users.getRoleId() != 2) {
            throw new AmsException(MessageCode.USER_NOT_AUTHORITY);
        }*/

        int result = facialRepository.deleteFacialByUserId(userId, requestList);

        if (result != 0)
            return "SUCCESS!";
        else
            return "FAILED!";
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setFacialData(SetFaceRequest request) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) throw new AmsException(MessageCode.USER_NOT_FOUND);

        Facial old = facialRepository.findFacialByUserIdActive(request.getUserId());
        if(old!=null){
            old.setStatus(Constants.STATUS_TYPE.DELETED);
            old.setModifiedAt(LocalDateTime.now());
            old.setModifiedBy(users.getId());
        }

        Facial facial = facialRepository.findFacialById(request.getUserId(), request.getFaceId());
        if(facial == null) throw new AmsException(MessageCode.FACE_NOT_FOUND);
        old.setStatus(Constants.STATUS_TYPE.ACTIVE);
        old.setModifiedAt(LocalDateTime.now());
        old.setModifiedBy(users.getId());
    }
}
