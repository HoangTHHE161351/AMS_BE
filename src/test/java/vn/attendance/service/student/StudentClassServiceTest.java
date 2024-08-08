package vn.attendance.service.student;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Curriculum;
import vn.attendance.model.Semester;
import vn.attendance.model.StudentClass;
import vn.attendance.model.Users;
import vn.attendance.repository.StudentClassRepository;
import vn.attendance.repository.StudentRepository;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.studentClass.request.AddStudentClassRequest;
import vn.attendance.service.studentClass.service.impl.StudentClassServiceImpl;
import vn.attendance.util.Constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class StudentClassServiceTest {
    @Mock
    private StudentClassRepository studentClassRepository;

    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentClassServiceImpl studentClassService;

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
    public void addStudentClassTestFail() throws AmsException {


        AddStudentClassRequest request = new AddStudentClassRequest(
                "khuongPBhe175867",
                "SE1730",
                null,
                null
        );

        AddStudentClassRequest response = studentClassService.addStudentClass(request, 2);

        Assertions.assertNotNull(response);
        assertEquals("FAILED", response.getStatus());

    }


    @Test
    void deleteStudentClass() throws AmsException {
        int id = 1;
        StudentClass studentClass = new StudentClass();
        studentClass.setId(id);
        studentClass.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(studentClassRepository.findById(id)).thenReturn(Optional.of(studentClass));
        studentClassService.deleteStudentClass(id);

        assertEquals(Constants.STATUS_TYPE.DELETED, studentClass.getStatus());
    }

}
