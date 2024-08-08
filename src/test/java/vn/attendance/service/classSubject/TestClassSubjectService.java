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
import vn.attendance.service.classSubject.request.EditClassSubjectRequest;
import vn.attendance.service.classSubject.response.ClassSubjectDto;

import vn.attendance.service.classSubject.service.impl.ClassSubjectImpl;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        classSubjectService.deleteClassSubject(id);
        assertEquals(Constants.STATUS_TYPE.DELETED, classSubject.getStatus());
    }
    @Test
    void deleteClassSubject_ClassSubjectNotFound() {
        Integer id = 1;
        when(classSubjectRepository.findById(id)).thenReturn(Optional.empty());

        AmsException exception = assertThrows(AmsException.class, () -> {
            classSubjectService.deleteClassSubject(id);
        });

        assertEquals(MessageCode.CLASS_SUBJECT_NOT_FOUND.getCode(), exception.getMessage());
    }
    @Test
    void testEditClassSubject_UserNotFound() {
        BaseUserDetailsService.USER.set(null);
        EditClassSubjectRequest request = new EditClassSubjectRequest();
        AmsException exception = assertThrows(AmsException.class, () -> {
            classSubjectService.editClassSubject(1, request);
        });
        assertEquals(MessageCode.USER_NOT_FOUND.getCode(), exception.getMessage());
    }

    @Test
    void testEditClassSubject_SubjectNotFound() throws AmsException {
        ClassSubject classSubject = new ClassSubject();
        classSubject.setId(1);
        classSubject.setStatus(Constants.STATUS_TYPE.ACTIVE);

        ClassRoom classRoom = new ClassRoom();
        classRoom.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(classSubjectRepository.findById(1)).thenReturn(Optional.of(classSubject));
        when(classRoomRepository.findById(1)).thenReturn(Optional.of(classRoom));
        when(subjectRepository.findById(1)).thenReturn(Optional.empty());

        EditClassSubjectRequest request = new EditClassSubjectRequest();
        request.setClassId(1);
        request.setSubjectId(1);

        EditClassSubjectRequest result = classSubjectService.editClassSubject(1, request);

        assertEquals("FAILED", result.getStatus());
        assertEquals(MessageCode.SUBJECT_NOT_FOUND.getCode(), result.getErrorMess());
    }
    @Test
    void testEditClassSubject_Success() throws AmsException {
        ClassSubject classSubject = new ClassSubject();
        classSubject.setId(1);
        classSubject.setClassId(1);
        classSubject.setSubjectId(1);
        classSubject.setStatus(Constants.STATUS_TYPE.ACTIVE);

        ClassRoom classRoom = new ClassRoom();
        classRoom.setId(1);
        classRoom.setStatus(Constants.STATUS_TYPE.ACTIVE);

        Subject subject = new Subject();
        subject.setId(1);
        subject.setStatus(Constants.STATUS_TYPE.ACTIVE);

        when(classSubjectRepository.findById(1)).thenReturn(Optional.of(classSubject));
        when(classRoomRepository.findById(1)).thenReturn(Optional.of(classRoom));
        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));
        when(classSubjectRepository.findAll()).thenReturn(Arrays.asList(classSubject));

        EditClassSubjectRequest request = new EditClassSubjectRequest();
        request.setClassId(1);
        request.setSubjectId(1);

        EditClassSubjectRequest result = classSubjectService.editClassSubject(1, request);

        assertEquals("SUCCESS", result.getStatus());

        verify(classSubjectRepository).save(any(ClassSubject.class));
    }
}