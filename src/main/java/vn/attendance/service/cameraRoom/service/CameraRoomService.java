package vn.attendance.service.cameraRoom.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.CameraRoom;
import vn.attendance.service.cameraRoom.request.AddCameraRoomRequest;
import vn.attendance.service.classRoom.request.AddClassRoomRequest;

public interface CameraRoomService {
    AddCameraRoomRequest addCameraRoom(AddCameraRoomRequest request) throws AmsException;
    AddCameraRoomRequest editCameraRoom(Integer id, AddCameraRoomRequest request) throws AmsException;
    void deleteCamera(Integer id) throws AmsException;
    Page<CameraRoom> findAllCameraRoom(int page, int size);
}
