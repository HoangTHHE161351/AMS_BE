package vn.attendance.service.mqtt.service;

import vn.attendance.exception.AmsException;
import vn.attendance.job.entity.ChangeRequest;
import vn.attendance.service.mqtt.entity.UserDto;

import java.util.List;

public interface MqttService {

    void CreateAddFaceRequest(Integer personType) throws AmsException;

    void CreateAddFaceRequest(ChangeRequest request) throws AmsException;
    void CreateAddFaceRequest(Integer deviceId, List<UserDto> userDtoList, Integer personType) throws AmsException, InterruptedException;

    void CreateDeleteFaceRequest(Integer userId) throws AmsException;

    void CreateDeleteFaceRequest() throws AmsException;

    void CreateChangeStateFaceRequest(Integer userId, Integer personType) throws AmsException;
    void CreateChangeStateFaceRequest(Integer userId, Integer devId ,Integer personType) throws AmsException;
}
