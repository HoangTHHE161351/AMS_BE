package vn.attendance.service.camera.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.service.camera.request.AddCameraRequest;
import vn.attendance.service.camera.request.EditCameraRequest;
import vn.attendance.service.camera.response.CameraRes;
import vn.attendance.service.camera.response.ICameraAccessRes;
import vn.attendance.service.camera.response.ICameraRes;
import vn.attendance.service.mqtt.entity.CameraDto;
import vn.attendance.service.semester.response.SemesterDto;

import java.util.List;


public interface CameraService {
    Page<CameraRes> findAllCamera(String search, String status, Integer roomId, int page, int size);
    AddCameraRequest addCamera(AddCameraRequest request) throws AmsException;
    void editCamera(EditCameraRequest request) throws AmsException;
    void deleteCamera(Integer cameraId) throws AmsException;

    List<AddCameraRequest> importCamera(List<AddCameraRequest> cameraList) throws AmsException;

    void getCameraAccess(Integer cameraId) throws AmsException;
    Page<CameraDto> findCamera(String search, Integer pageNo, Integer pageSize);


    void getCameraAccessByRoom(Integer roomId) throws AmsException;
}
