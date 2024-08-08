
        package vn.attendance.service.student;

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
import vn.attendance.model.StudentCurriculum;
import vn.attendance.model.Users;
import vn.attendance.repository.StudentCurriculumRepository;
import vn.attendance.repository.StudentRepository;
import vn.attendance.service.student.StudentServiceImpl;
import vn.attendance.service.student.request.AddStudentCurriculum;
import vn.attendance.service.student.request.EditStudentCurriculum;
import vn.attendance.service.student.response.StudentDto;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentCurriculumRepository studentCurriculumRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Users testUser;

    @BeforeEach
    public void setUp() {
        testUser = new Users();
        testUser.setRoleId(1);
        testUser.setId(1);
        BaseUserDetailsService.USER.set(testUser);
        MockitoAnnotations.openMocks(this);
    }


    // Test case to verify addition of a student curriculum when the student is not found
    @Test
    public void addStudentCurriculumTestStudentNotFound() {
        AddStudentCurriculum request = new AddStudentCurriculum();
        request.setCourseId(1);
        request.setStudentId(1);
        request.setDescription("Description");
        request.setStatus("");

        when(studentRepository.findStudentById(request.getStudentId())).thenReturn(Optional.empty());

        AmsException thrownException = assertThrows(AmsException.class, () -> {
            studentService.addStudentCurriculum(request);
        });

        assertEquals("Student not found!", thrownException.getCode());
    }

    // Test case to verify addition of a student curriculum when the student already has a curriculum
    @Test
    public void addStudentCurriculumTestStudentAlreadyHaveCurriculum() {
        AddStudentCurriculum request = new AddStudentCurriculum();
        request.setCourseId(1);
        request.setStudentId(1);
        request.setDescription("Description");
        request.setStatus("");

        StudentCurriculum existingCurriculum = new StudentCurriculum();
        existingCurriculum.setCurriculumId(1);

        StudentDto s = new StudentDto();
        s.setCurriculumName("abc");

        when(studentRepository.findStudentById(request.getStudentId())).thenReturn(Optional.of(s));

        AmsException thrownException = assertThrows(AmsException.class, () -> {
            studentService.addStudentCurriculum(request);
        });
        assertEquals(MessageCode.STUDENT_ALREADY_HAVE_CURRICULUM.getCode(), thrownException.getCode());
    }

    @Test
    public void updateStudentCurriculumTestStudentAlreadyHaveCurriculum() throws AmsException {
        Integer studentCurriculumId = 1;
        EditStudentCurriculum request = new EditStudentCurriculum();
        request.setCourseId(1); // Same course ID to simulate no change
        request.setDescription("New Description");
        request.setStatus("");
        StudentCurriculum existingCurriculum = new StudentCurriculum();
        existingCurriculum.setCurriculumId(1);
        existingCurriculum.setStatus(Constants.STATUS_TYPE.ACTIVE);
        when(studentCurriculumRepository.findById(studentCurriculumId)).thenReturn(Optional.of(existingCurriculum));
        EditStudentCurriculum response = studentService.updateStudentCurriculum(studentCurriculumId, request);
        assertEquals(Constants.REQUEST_STATUS.FAILED, response.getStatus());
        assertEquals(MessageCode.STUDENT_ALREADY_HAVE_CURRICULUM.toString(), response.getErrorMess());
    }

    @Test
    public void updateStudentCurriculumTestSuccess() throws AmsException {
        Integer studentCurriculumId = 1;
        EditStudentCurriculum request = new EditStudentCurriculum();
        request.setCourseId(2);
        request.setDescription("New Description");
        request.setStatus("");

        StudentCurriculum existingCurriculum = new StudentCurriculum();
        existingCurriculum.setCurriculumId(1);
        existingCurriculum.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(studentCurriculumRepository.findById(studentCurriculumId)).thenReturn(Optional.of(existingCurriculum));
        when(studentCurriculumRepository.save(any(StudentCurriculum.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EditStudentCurriculum response = studentService.updateStudentCurriculum(studentCurriculumId, request);

        assertEquals("SUCCESS", response.getStatus());
    }

    @Test
    public void updateStudentCurriculumTestCurriculumNotFound() {
        Integer curriculumId = 1;
        EditStudentCurriculum request = new EditStudentCurriculum();
        request.setCourseId(1);
        request.setDescription("Description");
        request.setStatus("");

        when(studentCurriculumRepository.findById(curriculumId)).thenReturn(Optional.empty());

        AmsException thrownException = assertThrows(AmsException.class, () -> {
            studentService.updateStudentCurriculum(curriculumId, request);
        });

        assertEquals("Curriculum not found!", thrownException.getCode());
    }
    @Test
    public void deleteStudentCurriculumTestNotFound() {
        Integer studentCurriculumId = 1;
        when(studentRepository.getStudentById(studentCurriculumId)).thenReturn(null);
        assertThrows(AmsException.class, () -> {
            studentService.deleteStudentCurriculum(studentCurriculumId);
        });
    }


}