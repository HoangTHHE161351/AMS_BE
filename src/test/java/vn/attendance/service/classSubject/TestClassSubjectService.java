package vn.attendance.service.classSubject;

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
import vn.attendance.model.ClassRoom;
import vn.attendance.model.ClassSubject;
import vn.attendance.model.Subject;
import vn.attendance.model.Users;
import vn.attendance.repository.ClassRoomRepository;
import vn.attendance.repository.ClassSubjectRepository;
import vn.attendance.repository.SubjectRepository;
import vn.attendance.service.classSubject.request.AddClassSubjectRequest;
import vn.attendance.service.classSubject.response.ClassSubjectDto;

import vn.attendance.service.classSubject.service.impl.ClassSubjectImpl;
import vn.attendance.util.Constants;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class TestClassSubjectService {

    @Mock
    ClassSubjectRepository classSubjectRepository;

    @Mock
    ClassRoomRepository classRoomRepository;

    @Mock
    SubjectRepository subjectRepository;

    @InjectMocks
    ClassSubjectImpl classSubjectService;

    private Users testUser;
    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setRoleId(1);
        testUser.setId(1);
        BaseUserDetailsService.USER.set(testUser);
    }

    @Test
    void findClassSubject() {
        String search = "test";
        int pageNo = 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<ClassSubjectDto> expectedPage = new PageImpl<>(Arrays.asList(new ClassSubjectDto()));

        when(classSubjectRepository.findClassSubject(search, pageable)).thenReturn(expectedPage);

        Page<ClassSubjectDto> result = classSubjectService.findClassSubject(search, pageNo, pageSize);

        assertEquals(expectedPage, result);
        verify(classSubjectRepository).findClassSubject(search, pageable);
    }

    @Test
    void addClassSubject() throws AmsException {
        AddClassSubjectRequest request = new AddClassSubjectRequest();
        request.setClassName("SE1730");
        request.setSubjectCode("SE101");

        ClassRoom classRoom = new ClassRoom();
        classRoom.setStatus(Constants.STATUS_TYPE.ACTIVE);

        Subject subject = new Subject();
        subject.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(classRoomRepository.findById(1)).thenReturn(Optional.of(classRoom));
        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));
        when(classSubjectRepository.findAll()).thenReturn(Arrays.asList());

        AddClassSubjectRequest result = classSubjectService.addClassSubject(request, 1);

        assertEquals("SUCCESS", result.getStatus());
        verify(classSubjectRepository).save(any(ClassSubject.class));
    }



    @Test
    void deleteClassSubject() throws AmsException {
        int id = 1;
        ClassSubject classSubject = new ClassSubject();
        classSubject.setId(id);
        classSubject.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(classSubjectRepository.findById(id)).thenReturn(Optional.of(classSubject));

        // Act
        classSubjectService.deleteClassSubject(id);

        // Assert
        assertEquals(Constants.STATUS_TYPE.DELETED, classSubject.getStatus());
    }



}