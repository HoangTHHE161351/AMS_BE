package vn.attendance.service.server.service;

import vn.attendance.exception.AmsException;
import vn.attendance.model.Camera;
import vn.attendance.service.server.dto.CameraRule;
import vn.attendance.service.server.response.CameraInfoDto;
import vn.attendance.service.server.response.DiskInfoDto;

import java.util.HashMap;
import java.util.List;

public interface DeviceService {
    List<DiskInfoDto> GetHardDiskState() throws AmsException;

    HashMap<String, CameraInfoDto> GetListCamera() throws AmsException;

    CameraRule GetCameraRule(Integer cameraId) throws AmsException;

    boolean AttachCameraState();

    boolean DetachCameraState();

    void SynchronizeCamera();

    Camera checkCamera(Camera camera);
}
