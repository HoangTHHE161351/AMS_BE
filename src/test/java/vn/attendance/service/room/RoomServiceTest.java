package vn.attendance.service.room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Room;
import vn.attendance.model.Semester;
import vn.attendance.model.TimeSlot;
import vn.attendance.model.Users;
import vn.attendance.repository.CameraRepository;
import vn.attendance.repository.RoomRepository;
import vn.attendance.service.room.request.AddRoomRequest;
import vn.attendance.service.room.request.EditRoomRequest;
import vn.attendance.service.room.response.RoomDto;
import vn.attendance.service.room.service.impl.RoomServiceImpl;
import vn.attendance.service.schedule.service.impl.ScheduleImp;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;
    // Mock the RoomRepository

    @Mock
    private CameraRepository cameraRepository;
    @InjectMocks
    private RoomServiceImpl roomService; // Inject mocks into RoomServiceImpl


    private Users testUser;

    @BeforeEach
    public void setUp() {

        testUser = new Users();
        testUser.setRoleId(1);
        testUser.setId(1);
        BaseUserDetailsService.USER.set(testUser);
        MockitoAnnotations.openMocks(this);

    }


    @Test
    public void addRoomTestSuccess() throws AmsException {
        when(roomRepository.findAllRooms()).thenReturn(new ArrayList<>());
        AddRoomRequest request = new AddRoomRequest("abc", "abc", "", "");
        AddRoomRequest response = roomService.addRoom(request, 1);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("", response.getErrorMess());
    }

    @Test
    void deleteRoom() throws AmsException {
        int id = 1;
        Room room = new Room();
        room.setId(id);
        room.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        when(cameraRepository.countCameraByRoom(id)).thenReturn(0); // Mocking no active cameras

        roomService.deleteRoom(id);

        assertEquals(Constants.STATUS_TYPE.DELETED, room.getStatus());
    }




}