package vn.attendance.service.curriculumSubject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.CurriculumSubject;
import vn.attendance.model.Room;
import vn.attendance.model.Users;

import vn.attendance.repository.CurriculumSubjectRepository;
import vn.attendance.service.curriculumSubject.request.AddCurriculumSubjectRequest;
import vn.attendance.service.curriculumSubject.request.EditCurriculumSubjectRequest;
import vn.attendance.service.curriculumSubject.service.impl.CurriculumSubjectServiceImpl;
import vn.attendance.util.Constants;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class TestCurriculumSubjectService {

    @Mock
    private CurriculumSubjectRepository curriculumSubjectRepository;

    @InjectMocks
    private CurriculumSubjectServiceImpl curriculumSubjectService;
    private Users testUser;
    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setRoleId(1);
        testUser.setId(1);
        BaseUserDetailsService.USER.set(testUser);
    }

    @Test
    void addCurriculumSubject_Success() throws AmsException {
        BaseUserDetailsService.USER.set(testUser);

        AddCurriculumSubjectRequest request = new AddCurriculumSubjectRequest();
        request.setCurriculum_id(1);
        request.setSubject_id(1);
        request.setSemester_id(1);

        when(curriculumSubjectRepository.findByCurriculumIdAndSubjectIdAndSemesterId(
                request.getCurriculum_id(), request.getSubject_id(), request.getSemester_id())).thenReturn(null);

        AddCurriculumSubjectRequest response = curriculumSubjectService.addCurriculumSubject(request);

        assertEquals("SUCCESS", response.getStatus());
//        verify(curriculumSubjectRepository, times(1)).save(any(CurriculumSubject.class));

    }

    @Test
    void testEditCurriculumSubject_Success() throws AmsException {
        BaseUserDetailsService.USER.set(testUser);

        Integer id = 1;
        EditCurriculumSubjectRequest request = new EditCurriculumSubjectRequest();
        request.setCurriculum_id(1);
        request.setSubject_id(1);
        request.setSemester_id(1);

        CurriculumSubject exist = new CurriculumSubject();
        exist.setCurriculumId(2);
        exist.setSubjectId(2);
        exist.setSemesterId(2);

        when(curriculumSubjectRepository.getById(id)).thenReturn(exist);

        EditCurriculumSubjectRequest response = curriculumSubjectService.editCurriculumSubject(id, request);

        assertEquals("SUCCESS", response.getStatus());
    }

    @Test
    void deleteCurriculumSubject() throws AmsException {
        int id = 1;
        CurriculumSubject curriculumSubject = new CurriculumSubject();
        curriculumSubject.setId(id);
        curriculumSubject.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(  curriculumSubjectRepository.findById(id)).thenReturn(Optional.of(curriculumSubject));


        curriculumSubjectService.deleteCurriculumSub(id);

        assertEquals(Constants.STATUS_TYPE.DELETED, curriculumSubject.getStatus());
    }



}