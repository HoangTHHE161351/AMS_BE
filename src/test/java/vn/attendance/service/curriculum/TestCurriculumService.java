package vn.attendance.service.curriculum;

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
import vn.attendance.model.Users;
import vn.attendance.repository.CurriculumRepository;
import vn.attendance.repository.CurriculumSubjectRepository;
import vn.attendance.service.curriculum.service.impl.CurriculumServiceImpl;
import vn.attendance.service.curriculumSubject.service.impl.CurriculumSubjectServiceImpl;
import vn.attendance.util.Constants;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
}
