package vn.attendance.service.subject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Semester;
import vn.attendance.model.Subject;
import vn.attendance.model.Users;
import vn.attendance.repository.ClassSubjectRepository;
import vn.attendance.repository.CurriculumSubjectRepository;
import vn.attendance.repository.SubjectRepository;
import vn.attendance.repository.TeacherSubjectRepository;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.semester.service.impl.SemesterServiceImpl;
import vn.attendance.service.subject.request.AddSubjectRequest;
import vn.attendance.service.subject.service.impl.SubjectServiceImpl;
import vn.attendance.util.Constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

}
