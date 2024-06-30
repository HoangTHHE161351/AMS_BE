package vn.attendance.service.subject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Semester;
import vn.attendance.model.Subject;
import vn.attendance.model.Users;
import vn.attendance.repository.SubjectRepository;
import vn.attendance.service.subject.request.AddSubjectRequest;
import vn.attendance.service.subject.request.EditSubjectRequest;
import vn.attendance.service.subject.response.SubjectDto;
import vn.attendance.service.subject.service.SubjectService;
import vn.attendance.service.teacher.service.response.TeacherDto;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    @Override
    public Optional<Subject> findById(Integer id) {
        return subjectRepository.findById(id);
    }

    @Override
    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    public Optional<Subject> updateSubject(Integer id, EditSubjectRequest editSubjectRequest) {
        return subjectRepository.findById(id).map(subject -> {
            subject.setCurriculumId(editSubjectRequest.getCurriculumId());
            subject.setName(editSubjectRequest.getName());
            subject.setCode(editSubjectRequest.getCode());
            subject.setModifiedAt(LocalDateTime.now());
//            subject.setModifiedBy(BaseUserDetailsService.USER.get().getId());
            subject.setSlots(editSubjectRequest.getSlots());
            return subjectRepository.save(subject);
        });
    }

    @Override
    public Page<Subject> findAllSubject(int page, int size) {
        return null;
    }

    @Override
    public List<Subject> findAllSubject() {
        return subjectRepository.findAllSubjects();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddSubjectRequest addSubject(AddSubjectRequest request) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        List<Subject> subjectList = subjectRepository.findAllSubject();

        for (Subject subject : subjectList) {

            if (subject.getCode().equals(request.getCode())) {
                request.setStatus("FAILED");
                request.setErrorMess(MessageCode.SUBJECT_CODE_ALREADY_EXISTS.getCode());
                return request;
            }
            if (subject.getName().equals(request.getName())) {
                request.setStatus("FAILED");
                request.setErrorMess(MessageCode.SUBJECT_NAME_ALREADY_EXISTS.getCode());
                return request;
            }
        }

        try {
            if (subjectRepository.existsByCode(request.getCode())) {
                throw new AmsException(MessageCode.SUBJECT_CODE_ALREADY_EXISTS);
            }


            Subject subject = new Subject();
            subject.setCode(request.getCode());
            subject.setName(request.getName());
            subject.setCreatedAt(LocalDateTime.now());
            subject.setStatus(request.getStatus());
            subject.setSlots(request.getSlots());
            subject.setCreatedBy(users.getId());
            subjectRepository.save(subject);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AmsException(MessageCode.ADD_SUBJECT_FAIL);
        }
        return request;
    }

//
    @Override
    public Page<SubjectDto> searchSubject(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return subjectRepository.searchSubject(search,  pageable);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<Subject> deleteSubject(Integer id) throws AmsException{
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        return subjectRepository.findById(id).map(sc -> {
            sc.setStatus(Constants.STATUS_TYPE.DELETED);
            sc.setModifiedAt(LocalDateTime.now());
            sc.setModifiedBy(user.getId());
            return subjectRepository.save(sc);
        });
    }
}
