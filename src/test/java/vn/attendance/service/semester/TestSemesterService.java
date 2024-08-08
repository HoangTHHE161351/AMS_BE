package vn.attendance.service.semester;

import jnr.ffi.annotations.In;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.*;
import vn.attendance.repository.SemesterRepository;
import vn.attendance.service.classSubject.request.EditClassSubjectRequest;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.semester.request.EditSemesterRequest;
import vn.attendance.service.semester.response.SemesterDto;
import vn.attendance.service.semester.service.impl.SemesterServiceImpl;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class TestSemesterService {

    @Mock
    private SemesterRepository semesterRepository;

    @InjectMocks
    private SemesterServiceImpl semesterService;

    private Users testUser;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setRoleId(1);
        testUser.setId(1);
        BaseUserDetailsService.USER.set(testUser);
    }

    @Test
    void findSemester() {
        String search = "FALL";
        int pageNo = 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<SemesterDto> expectedPage = new PageImpl<>(Arrays.asList(new SemesterDto()));

        when(semesterRepository.searchSemester(search,"", pageable)).thenReturn(expectedPage);

        Page<SemesterDto> result = semesterService.searchSemester(search, null, pageNo, pageSize);

        assertEquals(expectedPage, result);
        verify(semesterRepository).searchSemester(search, "",pageable);
    }

    @Test
    void deleteSemester() throws AmsException {
        int id = 1;
        Semester semester = new Semester();
        semester.setId(id);
        semester.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(semesterRepository.findById(id)).thenReturn(Optional.of(semester));
        semesterService.deleteSemester(id);

        assertEquals(Constants.STATUS_TYPE.DELETED, semester.getStatus());
    }
    @Test
    void deleteSemester_NotFound() {
        int id = 1;

        when(semesterRepository.findById(id)).thenReturn(Optional.empty());

        AmsException exception = assertThrows(AmsException.class, () -> {
            semesterService.deleteSemester(id);
        });

        assertEquals(MessageCode.SEMESTER_NOT_FOUND.getCode(), exception.getMessage());
    }
    @Test
    public void addSemesterTestFailByStartTime() throws AmsException {

        when(semesterRepository.findAllSemesters()).thenReturn(new ArrayList<>());

        AddSemesterRequest request = new AddSemesterRequest(
                "abc",
                LocalDate.parse("2023-08-01"),
                LocalDate.parse("2024-12-02"),
                "new Semester",
                null,
                null
        );

        AddSemesterRequest response = semesterService.addSemester(request, 2);

        Assertions.assertNotNull(response);
        assertEquals("FAILED", response.getStatus());

    }


    @Test
    public void addSemesterTestFailByEndTime() throws AmsException {

        when(semesterRepository.findAllSemesters()).thenReturn(new ArrayList<>());

        AddSemesterRequest request = new AddSemesterRequest(
                "abc",
                LocalDate.parse("2023-08-01"),
                LocalDate.parse("2023-07-02"),
                "new Semester",
                null,
                null
        );

        AddSemesterRequest response = semesterService.addSemester(request, 2);

        Assertions.assertNotNull(response);
        assertEquals("FAILED", response.getStatus());

    }

    @Test
    public void addSemesterTestFailBySemesterName() throws AmsException {

        when(semesterRepository.findAllSemesters()).thenReturn(new ArrayList<>());

        AddSemesterRequest request = new AddSemesterRequest(
               null,
                LocalDate.parse("2023-08-01"),
                LocalDate.parse("2023-07-02"),
                "new Semester",
                null,
                null
        );

        AddSemesterRequest response = semesterService.addSemester(request, 2);

        Assertions.assertNotNull(response);
        assertEquals("FAILED", response.getStatus());

    }

    @Test
    public void addSemesterTestSuccess() throws AmsException {

        when(semesterRepository.findAllSemesters()).thenReturn(new ArrayList<>());

        AddSemesterRequest request = new AddSemesterRequest(
                "abc",
                LocalDate.parse("2024-09-01"),
                LocalDate.parse("2024-12-02"),
                "new Semester",
                null,
                null
        );

        AddSemesterRequest response = semesterService.addSemester(request, 2);

        Assertions.assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertNull(response.getErrorMess());
    }

    @Test
    public void updateSemesterTestSuccess() throws AmsException {
        EditSemesterRequest request = new EditSemesterRequest(
                1,
                "Spring 2025",
                LocalDate.parse("2025-01-01"),
                LocalDate.parse("2025-05-01"),
                "Updated Semester",
                Constants.STATUS_TYPE.ACTIVE,
                ""
        );

        Semester existingSemester = new Semester();
        existingSemester.setId(1);
        existingSemester.setSemesterName("Spring 2025");
        existingSemester.setStartTime(LocalDateTime.parse("2025-01-01T00:00:00"));
        existingSemester.setEndTime(LocalDateTime.parse("2025-05-01T00:00:00"));
        existingSemester.setDescription("Updated Semester");
        existingSemester.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(semesterRepository.findById(request.getId())).thenReturn(Optional.of(existingSemester));
        when(semesterRepository.findSemestersByName(request.getSemesterName())).thenReturn(null);

        EditSemesterRequest response = semesterService.updateSemester(request);

        assertNotNull(response);
        assertEquals(Constants.REQUEST_STATUS.SUCCESS, response.getStatus());
    }

    @Test
    public void updateSemester_NotFound() {
        EditSemesterRequest request = new EditSemesterRequest(
                1,
                "Spring 2025",
                LocalDate.parse("2025-01-01"),
                LocalDate.parse("2025-05-01"),
                "Updated Semester",
                Constants.STATUS_TYPE.ACTIVE,
                ""
        );

        when(semesterRepository.findById(request.getId())).thenReturn(Optional.empty());

        AmsException exception = assertThrows(AmsException.class, () -> {
            semesterService.updateSemester(request);
        });

        assertEquals(MessageCode.SEMESTER_NOT_FOUND.getCode(), exception.getMessage());
    }





}
