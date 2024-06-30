package vn.attendance.service.camera.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Camera;
import vn.attendance.service.camera.request.AddCameraRequest;


public interface CameraService {
    Page<Camera> findAllCamera(int page, int size);
    void addCamera(AddCameraRequest request) throws AmsException;
    void editCamera(AddCameraRequest request) throws AmsException;
    void deleteCamera(Integer cameraId) throws AmsException;
}
