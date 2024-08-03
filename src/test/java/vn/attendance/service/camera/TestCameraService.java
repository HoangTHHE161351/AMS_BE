package vn.attendance.service.camera;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Camera;
import vn.attendance.model.Curriculum;
import vn.attendance.model.Semester;
import vn.attendance.model.Users;
import vn.attendance.repository.CameraRepository;
import vn.attendance.service.camera.request.AddCameraRequest;
import vn.attendance.service.camera.service.impl.CameraServiceImpl;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class TestCameraService {
    @Mock
    private CameraRepository cameraRepository;
    @InjectMocks
    private CameraServiceImpl cameraService;
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


}
