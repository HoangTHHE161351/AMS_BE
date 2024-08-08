package vn.attendance.service.teacherSubject;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Subject;
import vn.attendance.model.TeacherSubject;
import vn.attendance.model.Users;
import vn.attendance.repository.ScheduleRepository;
import vn.attendance.repository.SubjectRepository;
import vn.attendance.repository.TeacherRepository;
import vn.attendance.repository.TeacherSubjectRepository;
import vn.attendance.service.classRoom.request.AddClassRoomRequest;
import vn.attendance.service.schedule.service.impl.ScheduleImp;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.semester.response.SemesterDto;
import vn.attendance.service.teacher.request.AddTeacherSubjectRequest;
import vn.attendance.service.teacherSubject.response.TeacherSubjectDto;
import vn.attendance.service.teacherSubject.service.impl.TeacherSubjectImpl;
import vn.attendance.util.Constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})

public class TeacherSubjectTestService {
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private TeacherSubjectRepository teacherSubjectRepository;
    @InjectMocks
    private TeacherSubjectImpl teacherSubjectService;
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
    void importTeacherSubject() throws AmsException {
        List<AddTeacherSubjectRequest> teacherSubjectRequests = new ArrayList<>();
        AddTeacherSubjectRequest request = new AddTeacherSubjectRequest();
        request.setTeacherCode("T001");
        request.setSubjectCode("S001");
        teacherSubjectRequests.add(request);
        Users mockTeacher = new Users();
        mockTeacher.setId(1);
        Subject mockSubject = new Subject();
        mockSubject.setId(1);
        when(teacherRepository.findTeacherByCode(request.getTeacherCode())).thenReturn(mockTeacher);
        when(subjectRepository.findSubjectByCode(request.getSubjectCode())).thenReturn(mockSubject);
        when(teacherSubjectRepository.findByTeacherAndSubId(mockTeacher.getId(), mockSubject.getId())).thenReturn(null);
        List<AddTeacherSubjectRequest> result = teacherSubjectService.importTeacherSubject(teacherSubjectRequests);

        assertNotNull(result);
        assertEquals(teacherSubjectRequests.size(), result.size());
        assertEquals(Constants.REQUEST_STATUS.SUCCESS, result.get(0).getStatus());
    }



}
