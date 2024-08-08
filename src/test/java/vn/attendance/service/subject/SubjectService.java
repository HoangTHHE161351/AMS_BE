package vn.attendance.service.subject;

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
import vn.attendance.model.Room;
import vn.attendance.model.Semester;
import vn.attendance.model.Subject;
import vn.attendance.model.Users;
import vn.attendance.repository.ClassSubjectRepository;
import vn.attendance.repository.CurriculumSubjectRepository;
import vn.attendance.repository.SubjectRepository;
import vn.attendance.repository.TeacherSubjectRepository;
import vn.attendance.service.room.request.AddRoomRequest;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.semester.service.impl.SemesterServiceImpl;
import vn.attendance.service.subject.request.AddSubjectRequest;
import vn.attendance.service.subject.response.SubjectDto;
import vn.attendance.service.subject.service.impl.SubjectServiceImpl;
import vn.attendance.service.teacher.request.AddTeacherSubjectRequest;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubjectService {


    @Mock
    private SubjectRepository subjectRepository; // Mock the AttendanceRepository

    @Mock
    private TeacherSubjectRepository teacherSubjectRepository;


    @Mock
    private ClassSubjectRepository classSubjectRepository;

    @Mock
    private CurriculumSubjectRepository curriculumSubjectRepository;



    @InjectMocks
    private SubjectServiceImpl subjectService;



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
    public void addSubjectTestSuccess() throws AmsException {
        AddSubjectRequest request = new AddSubjectRequest(
            "code1",
                "PRN222",
                45,
               null,
                null
        );

        AddSubjectRequest response = subjectService.addSubject(request, 2);

        Assertions.assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertNull(response.getErrorMess());
    }


    @Test
    void deleteSubject() throws AmsException {
        int id = 1;
        Subject subject = new Subject();
        subject.setId(id);
        subject.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));

        when(teacherSubjectRepository.findBySubject(id)).thenReturn(List.of());
        when(classSubjectRepository.findBySubject(id)).thenReturn(List.of());
        when(curriculumSubjectRepository.findBySubject(id)).thenReturn(List.of());
        subjectService.deleteSubject(id);

        assertEquals(Constants.STATUS_TYPE.DELETED, subject.getStatus());
    }
    @Test
    void importSubject() throws AmsException {
        // Tạo danh sách các yêu cầu thêm môn học
        List<AddSubjectRequest> subjectRequests = new ArrayList<>();
        AddSubjectRequest request = new AddSubjectRequest();
        request.setCode("S001");
        request.setName("Mathematics");
        request.setSlots(3);
        subjectRequests.add(request);
        when(subjectRepository.findSubjectByCode(request.getCode())).thenReturn(null);

        List<AddSubjectRequest> result = subjectService.importSubject(subjectRequests);
        assertNotNull(result);
        assertEquals(subjectRequests.size(), result.size());
        assertEquals(Constants.REQUEST_STATUS.SUCCESS, result.get(0).getStatus());
    }
    @Test
    public void addSubjectTestUserNotFound() {
        AddSubjectRequest request = new AddSubjectRequest(
                "code1",
                "PRN222",
                45,
                null,
                null
        );

        BaseUserDetailsService.USER.set(null);

        AmsException thrownException = assertThrows(AmsException.class, () -> {
            subjectService.addSubject(request, 2);
        });

        assertEquals(MessageCode.USER_NOT_FOUND.getCode(), thrownException.getCode());
    }
    @Test
    public void addSubjectTestSubjectCodeAlreadyExists() throws AmsException {
        AddSubjectRequest request = new AddSubjectRequest(
                "code1",
                "PRN222",
                45,
                null,
                null
        );

        when(subjectRepository.findSubjectByCode(request.getCode())).thenReturn(new Subject());

        subjectService.addSubject(request, 2);

        assertEquals(Constants.REQUEST_STATUS.FAILED, request.getStatus());
        assertEquals(MessageCode.SUBJECT_CODE_ALREADY_EXISTS.getCode(), request.getErrorMess());
    }

    @Test
    public void deleteSubjectTestUserNotFound() {
        int id = 1;
        BaseUserDetailsService.USER.set(null);

        AmsException thrownException = assertThrows(AmsException.class, () -> {
            subjectService.deleteSubject(id);
        });

        assertEquals(MessageCode.USER_NOT_FOUND.getCode(), thrownException.getCode());
    }
    @Test
    public void deleteSubjectTestSubjectNotFound() {
        int id = 1;

        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        AmsException thrownException = assertThrows(AmsException.class, () -> {
            subjectService.deleteSubject(id);
        });

        assertEquals(MessageCode.SUBJECT_NOT_FOUND.getCode(), thrownException.getCode());
    }
    @Test
    public void deleteSubjectTestSchedulesExistForSubject() {
        int id = 1;
        Subject subject = new Subject();
        subject.setId(id);
        subject.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(subjectRepository.countScheduleBySubject(id, LocalDate.now())).thenReturn(1);

        AmsException thrownException = assertThrows(AmsException.class, () -> {
            subjectService.deleteSubject(id);
        });

        assertEquals(MessageCode.SCHEDULES_EXIST_FOR_SUBJECT.getCode(), thrownException.getCode());
    }

//    @Test
//    public void searchSubjectTestSuccess() {
//        String search = "Math";
//        int page = 1;
//        int size = 10;
//        Pageable pageable = PageRequest.of(page - 1, size);
//        Page<SubjectDto> expectedPage = new PageImpl<>(Arrays.asList(new SubjectDto()));
//
//        when(subjectRepository.searchSubject(search, pageable)).thenReturn(expectedPage);
//
//        Page<SubjectDto> result = subjectService.searchSubject(search, page, size);
//
//        assertEquals(expectedPage, result);
//        verify(subjectRepository).searchSubject(search, pageable);
//    }
//
//    // Test case to verify search for subjects with no results
//    @Test
//    public void searchSubjectTestNoResults() {
//        String search = "NonExistentSubject";
//        int page = 1;
//        int size = 10;
//        Pageable pageable = PageRequest.of(page - 1, size);
//        Page<SubjectDto> expectedPage = new PageImpl<>(Collections.emptyList());
//
//        when(subjectRepository.searchSubject(search, pageable)).thenReturn(expectedPage);
//
//        Page<SubjectDto> result = subjectService.searchSubject(search, page, size);
//
//        assertEquals(expectedPage, result);
//        verify(subjectRepository).searchSubject(search, pageable);
//    }
//
//    // Test case to verify search for subjects with an empty search string
//    @Test
//    public void searchSubjectTestEmptySearchString() {
//        String search = "";
//        int page = 1;
//        int size = 10;
//        Pageable pageable = PageRequest.of(page - 1, size);
//        Page<SubjectDto> expectedPage = new PageImpl<>(Arrays.asList(new SubjectDto()));
//
//        when(subjectRepository.searchSubject(search, pageable)).thenReturn(expectedPage);
//
//        Page<SubjectDto> result = subjectService.searchSubject(search, page, size);
//
//        assertEquals(expectedPage, result);
//        verify(subjectRepository).searchSubject(search, pageable);
//    }
//
//    // Test case to verify successful finding of subjects
//
//
//    // Test case to verify finding of subjects with no results
//    @Test
//    public void findSubjectTestNoResults() {
//        String search = "UnknownSubject";
//        int page = 1;
//        int size = 10;
//        Pageable pageable = PageRequest.of(page - 1, size);
//        Page<SubjectDto> expectedPage = new PageImpl<>(Collections.emptyList());
//
//        when(subjectRepository.searchSubject(search, pageable)).thenReturn(expectedPage);
//
//        Page<SubjectDto> result = subjectService.findSubject(search, page, size);
//
//        assertEquals(expectedPage, result);
//        verify(subjectRepository).searchSubject(search, pageable);
//    }
//
//    // Test case to verify finding of subjects with an empty search string
//    @Test
//    public void findSubjectTestEmptySearchString() {
//        String search = "";
//        int page = 1;
//        int size = 10;
//        Pageable pageable = PageRequest.of(page - 1, size);
//        Page<SubjectDto> expectedPage = new PageImpl<>(Arrays.asList(new SubjectDto()));
//
//        when(subjectRepository.searchSubject(search, pageable)).thenReturn(expectedPage);
//
//        Page<SubjectDto> result = subjectService.findSubject(search, page, size);
//
//        assertEquals(expectedPage, result);
//        verify(subjectRepository).searchSubject(search, pageable);
//    }
}
