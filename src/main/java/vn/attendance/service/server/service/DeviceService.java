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

    HashMap<String, CameraInfoDto> GetListCameraCCTV() throws AmsException;

    CameraRule GetCameraRule(Integer cameraId) throws AmsException;

    void AttachCameraState();

    boolean DetachCameraState();

    void SynchronizeCamera();

    void synchronizeCameraCCTV();
    void disconectCameraCCTV();

    Camera checkCamera(Camera camera);

    void saveIvssServer(String deviceIp, Integer devicePort, String deviceUsername, String devicePassword);

    void changStatusIVSS(String status) throws AmsException;
}
