package vn.attendance.service.curriculum;

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
import vn.attendance.model.Curriculum;
import vn.attendance.model.CurriculumSubject;
import vn.attendance.model.Subject;
import vn.attendance.model.Users;
import vn.attendance.repository.CurriculumRepository;
import vn.attendance.repository.CurriculumSubjectRepository;
import vn.attendance.service.curriculum.request.AddCurriculumRequest;
import vn.attendance.service.curriculum.request.EditCurriculumRequest;
import vn.attendance.service.curriculum.service.impl.CurriculumServiceImpl;
import vn.attendance.service.curriculumSubject.service.impl.CurriculumSubjectServiceImpl;
import vn.attendance.service.schedule.request.AddScheduleRequest;
import vn.attendance.service.semester.request.AddSemesterRequest;
import vn.attendance.service.subject.request.AddSubjectRequest;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class TestCurriculumService {

    @Mock
    private CurriculumRepository curriculumRepository;

    @InjectMocks
    private CurriculumServiceImpl curriculumService;

    private Users testUser;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setRoleId(1);
        testUser.setId(1);

        BaseUserDetailsService.USER.set(testUser);

    }


    @Test
    void deleteCurriculum() throws AmsException {
        int id = 1;
        Curriculum curriculum = new Curriculum();
        curriculum.setId(id);
        curriculum.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(curriculumRepository.findById(id)).thenReturn(Optional.of(curriculum));
        curriculumService.deleteCurriculum(id);

        assertEquals(Constants.STATUS_TYPE.DELETED, curriculum.getStatus());
    }
    @Test
    void findById_CurriculumNotFound() {
        Integer id = 1;
        when(curriculumRepository.findById(id)).thenReturn(Optional.empty());

        AmsException exception = assertThrows(AmsException.class, () -> {
            curriculumService.findById(id);
        });

        assertEquals(MessageCode.CURRICULUM_NOT_FOUND.getCode(), exception.getMessage());
    }
    @Test
    void findAllCurriculum() {
        List<Curriculum> curriculums = new ArrayList<>();
        when(curriculumRepository.findAllCurriculum()).thenReturn(curriculums);

        List<Curriculum> result = curriculumService.findAllCurriculum();

        assertNotNull(result);
        assertEquals(curriculums, result);
    }

    @Test
    void deleteCurriculum_CurriculumNotFound() {
        Integer id = 1;
        when(curriculumRepository.findById(id)).thenReturn(Optional.empty());

        AmsException exception = assertThrows(AmsException.class, () -> {
            curriculumService.deleteCurriculum(id);
        });

        assertEquals(MessageCode.CURRICULUM_NOT_FOUND.getCode(), exception.getMessage());
    }

    @Test
    void deleteCurriculum_StudentsEnrolled() {
        Integer id = 1;
        Curriculum curriculum = new Curriculum();
        curriculum.setStatus(Constants.STATUS_TYPE.ACTIVE);
        when(curriculumRepository.findById(id)).thenReturn(Optional.of(curriculum));
        when(curriculumRepository.getCountStudentCurriculum(id)).thenReturn(1);

        AmsException exception = assertThrows(AmsException.class, () -> {
            curriculumService.deleteCurriculum(id);
        });

        assertEquals(MessageCode.STUDENTS_ENROLLED_IN_CURRICULUM.getCode(), exception.getMessage());
    }

    @Test
    void addCurriculum_NameExists() {
        AddCurriculumRequest request = new AddCurriculumRequest();
        request.setCurriculumName("Math");
        request.setDescription("Math Curriculum");
        request.setStatus(null);
        Curriculum existingCurriculum = new Curriculum();
        existingCurriculum.setCurriculumName("Math");
        when(curriculumRepository.findCurriculumByName(request.getCurriculumName())).thenReturn(existingCurriculum);

        AmsException exception = assertThrows(AmsException.class, () -> {
            curriculumService.addCurriculum(request, 2);
        });

        assertEquals(MessageCode.CURRICULUM_NAME_EXIST.getCode(), exception.getMessage());
    }

    @Test
    void editCurriculum_CurriculumNotFound() {
        EditCurriculumRequest request = new EditCurriculumRequest();
        request.setId(1); request.setCurriculumName("Math Updated"); request.setDescription("Updated Description");

        when(curriculumRepository.findById(request.getId())).thenReturn(Optional.empty());

        AmsException exception = assertThrows(AmsException.class, () -> {
            curriculumService.editCurriculum(request);
        });

        assertEquals(MessageCode.CURRICULUM_NOT_FOUND.getCode(), exception.getMessage());
    }





}
