package vn.attendance.service.schedule;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.*;
import vn.attendance.repository.*;
import vn.attendance.service.schedule.request.AddScheduleRequest;
import vn.attendance.service.schedule.service.impl.ScheduleImp;
import vn.attendance.service.studentClass.service.impl.StudentClassServiceImpl;
import vn.attendance.service.subject.request.AddSubjectRequest;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})

public class ScheduleTestService {
    @Mock
    private ScheduleRepository scheduleRepository;


    @Mock
    ClassRoomRepository classRoomRepository;

    @Mock
    SubjectRepository subjectRepository;

    @Mock
    TimeSlotRepository timeSlotRepository;

    @Mock
    RoomRepository roomRepository;

    @Mock
    SemesterRepository semesterRepository;

    @Mock
    TeacherRepository teacherRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    StudentClassRepository studentClassRepository;

    @Mock
    ClassSubjectRepository classSubjectRepository;

    @Mock
    TeacherSubjectRepository teacherSubjectRepository;

    @Mock
    AttendanceRepository attendanceRepository;

    @InjectMocks
    private ScheduleImp scheduleService;

    private Users testUser;


    @BeforeEach
    public void setUp() {
        testUser = new Users();
        testUser.setRoleId(1);
        testUser.setId(1);
        BaseUserDetailsService.USER.set(testUser);
        MockitoAnnotations.openMocks(this);

        Map<String, Integer> semesterIndexMap = new HashMap<>();
        Map<String, Integer> teacherIndexMap = new HashMap<>();
        Map<String, Integer> roomIndexMap = new HashMap<>();
        Map<String, Integer> classIndexMap = new HashMap<>();
        Map<String, Integer> timeSlotIndexMap = new HashMap<>();
        Map<String, Integer> subIndexMap = new HashMap<>();

        semesterIndexMap.put("FALL", 1);
        teacherIndexMap.put("thangtd89", 3);
        roomIndexMap.put("23h", 1);
        classIndexMap.put("SE1730", 1);
        timeSlotIndexMap.put("Slot 1", 1);
        subIndexMap.put("Introduction to Software Engineering", 1);

        ReflectionTestUtils.setField(scheduleService, "semesterIndexMap", semesterIndexMap);
        ReflectionTestUtils.setField(scheduleService, "teacherIndexMap", teacherIndexMap);
        ReflectionTestUtils.setField(scheduleService, "roomIndexMap", roomIndexMap);
        ReflectionTestUtils.setField(scheduleService, "classIndexMap", classIndexMap);
        ReflectionTestUtils.setField(scheduleService, "timeSlotIndexMap", timeSlotIndexMap);
        ReflectionTestUtils.setField(scheduleService, "subIndexMap", subIndexMap);
        Room room = new Room();
        room.setId(1);
        room.setRoomName("23h");

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(1);
        timeSlot.setSlotName("Slot 1");

        Subject subject = new Subject();
        subject.setId(1);
        subject.setCode("SE101");

        Semester semester = new Semester();
        semester.setId(1);
        semester.setSemesterName("FALL");

        ClassSubject classSubject = new ClassSubject();
        classSubject.setClassId(1);
        classSubject.setSubjectId(1);

        TeacherSubject teacherSubject = new TeacherSubject();
        teacherSubject.setTeacherId(1);
        teacherSubject.setSubjectId(1);

        when(roomRepository.findByName("23h")).thenReturn(room);
        when(timeSlotRepository.findByTimeSlotName("SE1730")).thenReturn(timeSlot);
        when(subjectRepository.findSubjectByCode("SE101")).thenReturn(subject);
        when(semesterRepository.findSemestersByName("FALL")).thenReturn(semester);
        when(classSubjectRepository.findByClassAndSubId(1, 1)).thenReturn(classSubject);
        when(teacherSubjectRepository.findByTeacherAndSubId(1, 1)).thenReturn(teacherSubject);


    }
    @Test
    void deleteSchedule() throws AmsException {
        int id = 1;
        Schedule schedule = new Schedule();
        schedule.setId(id);
        schedule.setStatus(Constants.STATUS_TYPE.ACTIVE);
        List<Attendance> attendances = new ArrayList<>();
        when(scheduleRepository.findById(id)).thenReturn(Optional.of(schedule));
        when(attendanceRepository.findAttendanceByScheduleId(id)).thenReturn(attendances);
        scheduleService.deleteSchedule(id);

        assertEquals(Constants.STATUS_TYPE.DELETED, schedule.getStatus());
    }



    @Test
    void addScheduleOption2() throws AmsException {

        AddScheduleRequest request = new AddScheduleRequest(
                "FALL",
                "SE1730",
                "SE101",
                "thangtd89",
                LocalDate.parse("2024-08-06"),
                "Slot 1",
                "DEL23",
                "NEW SCHEDULE",
                null,
                null
        );

        AddScheduleRequest response = scheduleService.addSchedule(2, request, "FALL", LocalDate.parse("2024-08-06"));

        assertNotNull(response);
        assertEquals(Constants.REQUEST_STATUS.FAILED, response.getStatus());

    }
}

