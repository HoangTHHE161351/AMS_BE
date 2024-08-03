package vn.attendance.service.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.StudentCurriculum;
import vn.attendance.model.Users;
import vn.attendance.repository.StudentCurriculumRepository;
import vn.attendance.repository.StudentRepository;
import vn.attendance.service.student.request.AddStudentCurriculum;
import vn.attendance.service.student.request.EditStudentCurriculum;
import vn.attendance.util.Constants;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    @Test
    public void updateStudentCurriculumTestSuccess() throws AmsException {
        Integer studentCurriculumId = 1;
        EditStudentCurriculum request = new EditStudentCurriculum();
        request.setCourseId(2); // Change course ID to simulate an update
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

        // Giả lập không tìm thấy sinh viên
        when(studentRepository.getStudentById(studentCurriculumId)).thenReturn(null);

        // Kiểm tra việc ném ngoại lệ AmsException
        assertThrows(AmsException.class, () -> {
            studentService.deleteStudentCurriculum(studentCurriculumId);
        });
    }

}