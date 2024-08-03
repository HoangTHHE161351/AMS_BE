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
import vn.attendance.model.Users;
import vn.attendance.repository.CurriculumRepository;
import vn.attendance.service.curriculum.request.AddCurriculumRequest;
import vn.attendance.service.curriculum.service.impl.CurriculumServiceImpl;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class TestCurriculumSubjectService {

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
    public void addCurriculumTestSuccess() throws AmsException {
        when(curriculumRepository.findAll()).thenReturn(new ArrayList<>());
        AddCurriculumRequest request = new AddCurriculumRequest(
                "COURSE1",
                "NEW CURRICULUM",

                null,
                null
        );

        AddCurriculumRequest response = curriculumService.addCurriculum(request, 2);

        Assertions.assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertNull(response.getErrorMes());
    }



}