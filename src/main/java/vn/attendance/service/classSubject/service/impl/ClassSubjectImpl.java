package vn.attendance.service.classSubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
import vn.attendance.service.classSubject.service.ClassSubjectService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClassSubjectImpl  implements ClassSubjectService {

    @Autowired
    ClassSubjectRepository classSubjectRepository;

    @Autowired
    ClassRoomRepository classRoomRepository;

    @Autowired
    SubjectRepository subjectRepository;





    @Override
    public ClassSubject findById(Integer id) throws AmsException {
        ClassSubject classSubject = classSubjectRepository.findById(id).orElse(null);
        if (classSubject == null) {
            throw new AmsException(MessageCode.NOT_FOUND);
        }
        return classSubject;
    }

    @Override
    public List<ClassSubject> findAllClassSubject() {
        return classSubjectRepository.findAll();
    }

    @Override
    public Page<ClassSubjectDto> findClassSubject(String search, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return classSubjectRepository.findClassSubject(search, pageable);
    }


}
