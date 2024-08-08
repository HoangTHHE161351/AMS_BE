package vn.attendance.service.camera;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.*;
import vn.attendance.repository.CameraRepository;
import vn.attendance.repository.RoomRepository;
import vn.attendance.service.camera.request.AddCameraRequest;
import vn.attendance.service.camera.request.EditCameraRequest;
import vn.attendance.service.camera.service.impl.CameraServiceImpl;
import vn.attendance.service.classRoom.request.AddClassRoomRequest;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.server.ServerInstance;
import vn.attendance.service.server.service.DeviceService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class TestCameraService {
    @Mock
    private CameraRepository cameraRepository;
    @Mock
    private RoomRepository roomRepository;
    @InjectMocks
    private CameraServiceImpl cameraService;

    @Mock
    private ServerInstance serverInstance;

    @Mock
    private DeviceService deviceService;
    private Users testUser;

    @Mock
    private BaseUserDetailsService baseUserDetailsService;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setRoleId(1);
        testUser.setId(1);
        BaseUserDetailsService.USER.set(testUser);
    }
    @Test
    void deleteCamera() throws AmsException {
        int id = 1;
        Camera camera = new Camera();
        camera.setId(id);
        camera.setStatus(Constants.STATUS_TYPE.ACTIVE);
        when(cameraRepository.getById(id)).thenReturn(camera);
        cameraService.deleteCamera(id);
        assertEquals(Constants.STATUS_TYPE.DELETED, camera.getStatus());
    }
    @Test
    void testAddCamera_InvalidIpFormat() throws Exception {
        AddCameraRequest request = new AddCameraRequest();
        request.setIpTcpip("invalid_ip");
        request.setPort(String.valueOf(8080));

        AddCameraRequest result = cameraService.addCamera(request);

        assertEquals(Constants.REQUEST_STATUS.FAILED, result.getStatus());
        assertEquals(MessageCode.FORMAT_FAIL.getCode(), result.getErrorMess());
    }

    // Camera already existed
    @Test
    void testAddCamera_CameraAlreadyExists() throws Exception {
        AddCameraRequest request = new AddCameraRequest();
        request.setIpTcpip("192.168.1.10");
        request.setPort(String.valueOf(8080));
        request.setRoomId(1);

        when(cameraRepository.getCameraByIpPort(request.getIpTcpip(), request.getPort())).thenReturn(new Camera());

        AddCameraRequest result = cameraService.addCamera(request);

        assertEquals(Constants.REQUEST_STATUS.FAILED, result.getStatus());
        assertEquals(MessageCode.CAMERA_ALREADY_EXISTED.getCode(), result.getErrorMess());
    }
    // ROOM_NOT_FOUND
    @Test
    void testAddCamera_RoomNotFound() throws Exception {
        AddCameraRequest request = new AddCameraRequest();
        request.setIpTcpip("192.168.1.10");
        request.setPort(String.valueOf(8080));
        request.setRoomId(1);

        when(cameraRepository.getCameraByIpPort(request.getIpTcpip(), request.getPort())).thenReturn(null);
        when(roomRepository.getById(request.getRoomId())).thenReturn(null);

        AddCameraRequest result = cameraService.addCamera(request);

        assertEquals(Constants.REQUEST_STATUS.FAILED, result.getStatus());
        assertEquals(MessageCode.ROOM_NOT_FOUND.getCode(), result.getErrorMess());
    }

    @Test
    void testAddCamera_CameraConnectionFailure() throws Exception {
        AddCameraRequest request = new AddCameraRequest();
        request.setIpTcpip("192.168.1.10");
        request.setPort(String.valueOf(8080));
        request.setRoomId(1);
        request.setCameraType(Constants.DEVICE_TYPE.CCTV);

        when(cameraRepository.getCameraByIpPort(request.getIpTcpip(), request.getPort())).thenReturn(null);
        when(roomRepository.getById(request.getRoomId())).thenReturn(new Room());
        when(serverInstance.isConnect()).thenReturn(true);
        when(deviceService.checkCamera(any(Camera.class))).thenReturn(null);

        AddCameraRequest result = cameraService.addCamera(request);

        assertEquals(Constants.REQUEST_STATUS.FAILED, result.getStatus());
        assertEquals(MessageCode.CAMERA_NOT_FOUND.getCode(), result.getErrorMess());
    }

    @Test
    void testEditCamera_InvalidIpFormat() {
        EditCameraRequest request = new EditCameraRequest();
        request.setIpTcpip("invalid_ip");
        request.setPort(String.valueOf(8080));

        AmsException thrownException = assertThrows(AmsException.class, () -> cameraService.editCamera(request));
        assertEquals(MessageCode.FORMAT_FAIL.getCode(), thrownException.getMessage());
    }

    // CAMERA_ALREADY_EXISTED
    @Test
    void testEditCamera_CameraAlreadyExists() {
        EditCameraRequest request = new EditCameraRequest();
        request.setIpTcpip("192.168.1.10");
        request.setPort(String.valueOf(8080));
        request.setId(2); // different from the existing camera id

        Camera existingCamera = new Camera();
        existingCamera.setId(1); // different id
        existingCamera.setIpTcip("192.168.1.10");
        existingCamera.setPort(String.valueOf(8080));

        when(cameraRepository.getCameraByIpPort(request.getIpTcpip(), request.getPort())).thenReturn(existingCamera);

        AmsException thrownException = assertThrows(AmsException.class, () -> cameraService.editCamera(request));
        assertEquals(MessageCode.CAMERA_ALREADY_EXISTED.getCode(), thrownException.getMessage());
    }
    @Test
    void deleteCamera_ExceptionCase() {
        // Arrange
        Integer cameraId = 1;
        when(cameraRepository.getById(cameraId)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(AmsException.class, () -> cameraService.deleteCamera(cameraId));
    }




}
