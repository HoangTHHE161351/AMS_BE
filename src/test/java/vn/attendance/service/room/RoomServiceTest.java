package vn.attendance.service.room;

import org.junit.jupiter.api.Assertions;
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
import vn.attendance.model.*;
import vn.attendance.repository.CameraRepository;
import vn.attendance.repository.RoomRepository;
import vn.attendance.service.classRoom.request.EditClassRoomRequest;
import vn.attendance.service.room.request.AddRoomRequest;
import vn.attendance.service.room.request.EditRoomRequest;
import vn.attendance.service.room.response.RoomDto;
import vn.attendance.service.room.service.impl.RoomServiceImpl;
import vn.attendance.service.schedule.service.impl.ScheduleImp;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    public void updateRoom_Success() throws AmsException {
        EditRoomRequest request = new EditRoomRequest(1, "Room 101", "Description", "", Constants.STATUS_TYPE.ACTIVE);
        Room existingRoom = new Room();
        existingRoom.setId(1);
        existingRoom.setStatus(Constants.STATUS_TYPE.ACTIVE);
        when(roomRepository.findById(request.getId())).thenReturn(Optional.of(existingRoom));
        when(roomRepository.findByName(request.getRoomName())).thenReturn(null);

        EditRoomRequest response = roomService.updateRoom(request);

        assertNotNull(response);
        assertEquals(Constants.REQUEST_STATUS.SUCCESS, response.getStatus());
    }
    @Test
    void deleteRoom() throws AmsException {
        int id = 1;
        Room room = new Room();
        room.setId(id);
        room.setStatus(Constants.STATUS_TYPE.ACTIVE);
        when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        when(cameraRepository.countCameraByRoom(id)).thenReturn(0);
        roomService.deleteRoom(id);
        assertEquals(Constants.STATUS_TYPE.DELETED, room.getStatus());
    }
    @Test
    void addRoomFailByRoomName() throws AmsException {

        AddRoomRequest request = new AddRoomRequest();
        request.setRoomName("Existing Room");
        request.setDescription("New Room Description");
        request.setStatus(null);
        request.setErrorMess(null);
        Room existingRoom = new Room();
        existingRoom.setRoomName("Existing Room");
        when(roomRepository.findByName(request.getRoomName())).thenReturn(existingRoom);
        AddRoomRequest response = roomService.addRoom(request, 2);
        assertNotNull(response);
        assertEquals(Constants.REQUEST_STATUS.FAILED, response.getStatus());
        assertEquals(MessageCode.ROOM_ALREADY_EXISTS.getCode(), response.getErrorMess());
    }
    @Test
    void addRoomFailByName() throws AmsException {

        AddRoomRequest request = new AddRoomRequest();
        request.setRoomName(null);
        request.setDescription("New Room Description");
        request.setStatus(null);
        request.setErrorMess(null);
        Room existingRoom = new Room();
        existingRoom.setRoomName("Existing Room");
        when(roomRepository.findByName(request.getRoomName())).thenReturn(existingRoom);

        AddRoomRequest response = roomService.addRoom(request, 2);
        assertNotNull(response);
        assertEquals(Constants.REQUEST_STATUS.FAILED, response.getStatus());
        assertEquals(MessageCode.ROOM_ALREADY_EXISTS.getCode(), response.getErrorMess());
    }




}