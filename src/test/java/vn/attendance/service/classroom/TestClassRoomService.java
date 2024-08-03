package vn.attendance.service.classroom;

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
import vn.attendance.model.ClassRoom;
import vn.attendance.model.Users;
import vn.attendance.repository.ClassRoomRepository;
import vn.attendance.service.classRoom.request.AddClassRoomRequest;
import vn.attendance.service.classRoom.request.EditClassRoomRequest;
import vn.attendance.service.classRoom.response.ClassRoomDto;
import vn.attendance.service.classRoom.service.impl.ClassRoomServiceImpl;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.util.Constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class TestClassRoomService {
    @Mock
    private ClassRoomRepository classRoomRepository;

    @InjectMocks
    private ClassRoomServiceImpl classRoomService;
    private Users testUser;
    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setRoleId(1);
        testUser.setId(1);
        BaseUserDetailsService.USER.set(testUser);
    }


    @Test
    void findAll() {
        List<ClassRoom> classRooms = new ArrayList<>();
        when(classRoomRepository.findAll()).thenReturn(classRooms);

        List<ClassRoom> result = classRoomService.findAll();

        assertNotNull(result);
        assertEquals(classRooms, result);
    }

    @Test
    void findById() throws AmsException {
        Integer id = 1;
        ClassRoom classRoom = new ClassRoom();
        when(classRoomRepository.findById(id)).thenReturn(java.util.Optional.of(classRoom));

        ClassRoom result = classRoomService.findById(id);

        assertNotNull(result);
        assertEquals(classRoom, result);
    }


    @Test
    void editClassRoom() throws AmsException {
        Integer id = 1;
        EditClassRoomRequest request = new EditClassRoomRequest();
        request.setClassName("Updated Class");
        request.setDescription("Updated Description");

        ClassRoom classRoom = new ClassRoom();
        when(classRoomRepository.getById(id)).thenReturn(classRoom);

        classRoomService.editClassRoom(id, request);

        assertEquals("SUCCESS", request.getStatus());
        verify(classRoomRepository, times(1)).save(classRoom);
    }

    @Test
    void deleteClassRoom() throws AmsException {
        Integer id = 1;
        ClassRoom classRoom = new ClassRoom();
        when(classRoomRepository.getById(id)).thenReturn(classRoom);

        classRoomService.deleteClassRoom(id);

        assertEquals(Constants.STATUS_TYPE.DELETED, classRoom.getStatus());
        verify(classRoomRepository, times(1)).save(classRoom);
    }

    @Test
    void exportClassRoom() throws AmsException {
        String search = "test";
        List<ClassRoomDto> classRoomDtos = new ArrayList<>();
        classRoomDtos.add(new ClassRoomDto());
        when(classRoomRepository.findAllToExport(search)).thenReturn(classRoomDtos);

        byte[] result = classRoomService.exportClassRoom(search);

        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void importClassRoom() throws AmsException {
        List<AddClassRoomRequest> classRoomRequests = new ArrayList<>();
        AddClassRoomRequest request = new AddClassRoomRequest();
        classRoomRequests.add(request);

        List<AddClassRoomRequest> result = classRoomService.importClassRoom(classRoomRequests);

        assertNotNull(result);
        assertEquals(classRoomRequests, result);
    }



    @Test
    public void addClassRoomTestSuccess() throws AmsException {

        when(classRoomRepository.findAll()).thenReturn(new ArrayList<>());

        AddClassRoomRequest request = new AddClassRoomRequest(
             "Class23",
                "New ClassRoom",
                null,
                null
        );

        AddClassRoomRequest response = classRoomService.addClassRoom(request, 2);

        Assertions.assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertNull(response.getErrorMess());
    }
}