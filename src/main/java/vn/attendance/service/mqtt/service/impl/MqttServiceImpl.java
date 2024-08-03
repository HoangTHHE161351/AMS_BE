package vn.attendance.service.mqtt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.attendance.config.mqtt.MqttSendRequest;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Camera;
import vn.attendance.repository.*;
import vn.attendance.job.entity.ChangeRequest;
import vn.attendance.service.mqtt.entity.*;
import vn.attendance.service.mqtt.service.MqttService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.util.List;

@Service
public class MqttServiceImpl implements MqttService {

    @Autowired
    CameraRepository cameraRepository;
    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    MqttSendRequest mqttSendRequest;

    @Override
    public void CreateAddFaceRequest(Integer personType) throws AmsException {
        List<UserDto> facList = usersRepository.getUserFace();
        for (UserDto user : facList) {
            SendAddRequest(user, personType, null);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void CreateAddFaceRequest(ChangeRequest request) throws AmsException {
        UserDto user = usersRepository.getUserFace(request.getUserId());
        if(user == null) throw new AmsException(MessageCode.USER_NOT_FOUND);

        Integer channelId = null;
        if (request.getDevId() != null) {
            CameraDto camera = cameraRepository.findLcdById(request.getDevId());
            if (camera == null) throw new AmsException(MessageCode.CAMERA_NOT_FOUND);
            channelId = camera.getChannelId();
        }

        SendAddRequest(user, request.getPersonType(), channelId);
    }


    @Override
    public void CreateAddFaceRequest(Integer deviceId, List<UserDto> userDtoList, Integer personType) throws AmsException, InterruptedException {
        for (UserDto user: userDtoList
             ) {
            SendAddRequest(user, personType, deviceId);
            Thread.sleep(1000);
        }
    }

    public void SendAddRequest(UserDto user, Integer personType, Integer deviceId){
        AddFaceRequest addFaceRequest = new AddFaceRequest();
        addFaceRequest.setCustomId(user.getUsername());
        addFaceRequest.setName(user.getFullName());
        addFaceRequest.setGender(user.getGender()-1);
        addFaceRequest.setCardType(0);
        addFaceRequest.setPersonType(personType);
        addFaceRequest.setDeviceId(deviceId);
        addFaceRequest.setTempCardType(0);
        addFaceRequest.setPic(user.getPic());
        mqttSendRequest.sendEditFaceRequest(addFaceRequest);
    }

    @Override
    public void CreateDeleteFaceRequest() throws AmsException {
        DelFaceRequest request = new DelFaceRequest();
        mqttSendRequest.sendDeleteFaceRequest(request);
    }

    @Override
    public void CreateDeleteFaceRequest(Integer userId) throws AmsException {
        UserDto user = usersRepository.getUserFace(userId);
        if(user == null) throw new AmsException(MessageCode.USER_NOT_FOUND);

        DelFaceRequest request = new DelFaceRequest();
        request.setCustomId(user.getUsername());
        mqttSendRequest.sendDeleteFaceRequest(request);
    }


    @Override
    public void CreateChangeStateFaceRequest(Integer userId, Integer personType) throws AmsException {
        UserDto user = usersRepository.getUserFace(userId);
        if(user == null) throw new AmsException(MessageCode.USER_NOT_FOUND);
        ChangeStateRequest changeStateRequest = new ChangeStateRequest();
        changeStateRequest.setCustomId(user.getUsername());
        changeStateRequest.setPersonType(personType);
        mqttSendRequest.sendChangeStateFaceRequest(changeStateRequest);
    }

    @Override
    public void CreateChangeStateFaceRequest(Integer userId, Integer devId, Integer personType) throws AmsException {
        UserDto user = usersRepository.getUserFace(userId);
        if(user == null) throw new AmsException(MessageCode.USER_NOT_FOUND);
        Camera camera = cameraRepository.findById(devId).orElse(null);
        if(camera == null || camera.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw  new AmsException(MessageCode.CAMERA_NOT_FOUND);
        }
        ChangeStateRequest changeStateRequest = new ChangeStateRequest();
        changeStateRequest.setCustomId(user.getUsername());
        changeStateRequest.setDeviceId(camera.getChannelId());
        changeStateRequest.setPersonType(personType);
        mqttSendRequest.sendChangeStateFaceRequest(changeStateRequest);
    }

}
