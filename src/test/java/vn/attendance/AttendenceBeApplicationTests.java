package vn.attendance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.model.Attendance;
import vn.attendance.model.Users;
import vn.attendance.repository.AttendanceRepository;
import vn.attendance.repository.RoomRepository;
import vn.attendance.repository.ScheduleRepository;
import vn.attendance.repository.TimeSlotRepository;
import vn.attendance.service.attendance.request.AttendanceDto;
import vn.attendance.service.attendance.request.EditAttendanceRequest;
import vn.attendance.service.attendance.request.IAttendanceDto;
import vn.attendance.service.attendance.response.AttendanceDTO;
import vn.attendance.service.attendance.response.IAttendanceDTO;
import vn.attendance.service.attendance.service.impl.AttendanceServiceImpl;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AttendenceBeApplicationTests {
    @Mock
    private AttendanceRepository attendanceRepository; // Mock the AttendanceRepository

    @InjectMocks
    private AttendanceServiceImpl attendanceService; // Inject mocks into AttendanceServiceImpl

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private TimeSlotRepository timeSlotRepository;

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
    public void testAttendanceReport_StudentRole() throws Exception {
        testUser.setRoleId(4);
        IAttendanceDto dto1 = mock(IAttendanceDto.class);
        IAttendanceDto dto2 = mock(IAttendanceDto.class);
        when(attendanceRepository.attendanceReportStudent(anyInt(), anyInt(), anyInt()))
                .thenReturn(Arrays.asList(dto1, dto2));
        List<AttendanceDto> result = attendanceService.attendanceReport(1, 1);
        assertEquals(2, result.size());
        verify(attendanceRepository, times(1)).attendanceReportStudent(anyInt(), anyInt(), anyInt());
    }

    @Test
    public void testAttendanceReport_TeacherRole() throws Exception {
        testUser.setRoleId(1);
        IAttendanceDto dto1 = mock(IAttendanceDto.class);
        IAttendanceDto dto2 = mock(IAttendanceDto.class);
        when(attendanceRepository.attendanceReportTeacher(anyInt(), anyInt(), anyInt()))
                .thenReturn(Arrays.asList(dto1, dto2));

        List<AttendanceDto> result = attendanceService.attendanceReport(1, 1);

        assertEquals(2, result.size());
        verify(attendanceRepository, times(1)).attendanceReportTeacher(anyInt(), anyInt(), anyInt());
    }
    @Test
    public void testListStudentAttendance() throws Exception {
        IAttendanceDTO dto1 = mock(IAttendanceDTO.class);
        IAttendanceDTO dto2 = mock(IAttendanceDTO.class);
        when(attendanceRepository.findAttendanceByClass(anyInt())).thenReturn(Arrays.asList(dto1, dto2));
        List<AttendanceDTO> result = attendanceService.listStudentAttendance(1);
        assertEquals(2, result.size());
    }

    @Test
    public void testCheckAttendance() throws Exception {
        List<EditAttendanceRequest> requestList = Arrays.asList(
                new EditAttendanceRequest(1, 1, "success", null),
                new EditAttendanceRequest(2, 0, "fail", null)
        );

        Attendance attendance1 = new Attendance();
        attendance1.setId(1);
        attendance1.setStatus("initial");

        Attendance attendance2 = new Attendance();
        attendance2.setId(2);
        attendance2.setStatus("initial");
        when(attendanceRepository.findAttendanceByScheduleId(anyInt()))
                .thenReturn(Arrays.asList(attendance1, attendance2));
        when(attendanceRepository.save(any(Attendance.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        List<EditAttendanceRequest> result = attendanceService.checkAttendance(requestList, 1);

        assertEquals(2, result.size());
        assertEquals("success", attendance1.getStatus());
        assertEquals("fail", attendance2.getStatus());
        assertEquals(1, requestList.get(0).getDescription());
        assertEquals(0, requestList.get(1).getDescription());

    }

}