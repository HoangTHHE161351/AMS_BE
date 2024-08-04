package vn.attendance.service.subject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.*;
import vn.attendance.repository.*;
import vn.attendance.service.subject.request.AddSubjectRequest;
import vn.attendance.service.subject.request.EditSubjectRequest;
import vn.attendance.service.subject.response.ISubjectDto;
import vn.attendance.service.subject.response.SubjectDto;
import vn.attendance.service.subject.service.SubjectService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    TeacherSubjectRepository teacherSubjectRepository;
    @Autowired
    CurriculumSubjectRepository curriculumSubjectRepository;
    @Autowired
    ClassSubjectRepository classSubjectRepository;
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
    public Subject updateSubject(EditSubjectRequest editSubjectRequest) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Subject subject = subjectRepository.findById(editSubjectRequest.getId()).orElse(null);
        if (subject == null || subject.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.SUBJECT_NOT_FOUND);
        }

        subject.setName(editSubjectRequest.getName());
        subject.setCode(editSubjectRequest.getCode());
        subject.setModifiedAt(LocalDateTime.now());
        subject.setModifiedBy(BaseUserDetailsService.USER.get().getId());
        subject.setSlots(editSubjectRequest.getSlots());


        return subjectRepository.save(subject);
    }

    @Override
    public List<ISubjectDto> getDropdownSubject() {
        return subjectRepository.getDropdownSubject();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddSubjectRequest addSubject(AddSubjectRequest request, Integer options) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        if (subjectRepository.findSubjectByCode(request.getCode()) != null) {
            if (options == 1) throw new AmsException(MessageCode.SUBJECT_CODE_ALREADY_EXISTS);

            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.SUBJECT_CODE_ALREADY_EXISTS.getCode());
            return request;
        }
/*        if (subjectRepository.findSubjectByName(request.getName()) != null) {
            if(options == 1) throw new AmsException(MessageCode.SUBJECT_NAME_ALREADY_EXISTS);

            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.SUBJECT_NAME_ALREADY_EXISTS.getCode());
            return request;
        }*/

        try {
            Subject subject = new Subject();
            subject.setCode(request.getCode());
            subject.setName(request.getName());
            subject.setCreatedAt(LocalDateTime.now());
            subject.setStatus(Constants.STATUS_TYPE.ACTIVE);
            subject.setSlots(request.getSlots());
            subject.setCreatedBy(users.getId());
            subjectRepository.save(subject);
        } catch (Exception e) {
            if (options == 1) throw new AmsException(MessageCode.ADD_SUBJECT_FAIL);
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            request.setErrorMess(MessageCode.ADD_SUBJECT_FAIL.getCode());
            return request;
        }
        request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
        return request;
    }

    @Override
    public List<AddSubjectRequest> importSubject(List<AddSubjectRequest> requests) throws AmsException {
        for (AddSubjectRequest request : requests
        ) {
            addSubject(request, 0);
        }
        return requests;
    }

    @Override
    public Page<SubjectDto> searchSubject(String search, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return subjectRepository.searchSubject(search, status, pageable);
    }

    @Override
    public Page<SubjectDto> findSubject(String search, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return subjectRepository.searchSubject(search, status, pageable);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Subject deleteSubject(Integer id) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        Subject subject = subjectRepository.findById(id).orElse(null);
        if (subject == null || subject.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
            throw new AmsException(MessageCode.SUBJECT_NOT_FOUND);
        }

        if (subjectRepository.countScheduleBySubject(id, LocalDate.now()) > 0)
            throw new AmsException(MessageCode.SCHEDULES_EXIST_FOR_SUBJECT);

        List<TeacherSubject> teacherSubjects = teacherSubjectRepository.findBySubject(id);
        for (TeacherSubject teacherSubject : teacherSubjects
        ) {
            teacherSubject.setStatus(Constants.STATUS_TYPE.DELETED);
            teacherSubject.setModifiedAt(LocalDateTime.now());
            teacherSubject.setModifiedBy(user.getId());
            teacherSubjectRepository.save(teacherSubject);
        }

        List<ClassSubject> classSubjects = classSubjectRepository.findBySubject(id);
        for (ClassSubject classSubject : classSubjects
        ) {
            classSubject.setStatus(Constants.STATUS_TYPE.DELETED);
            classSubject.setModifiedAt(LocalDateTime.now());
            classSubject.setModifiedBy(user.getId());
            classSubjectRepository.save(classSubject);
        }

        List<CurriculumSubject> curriculumSubjects = curriculumSubjectRepository.findBySubject(id);
        for (CurriculumSubject curriculumSubject : curriculumSubjects
        ) {
            curriculumSubject.setStatus(Constants.STATUS_TYPE.DELETED);
            curriculumSubject.setModifiedAt(LocalDateTime.now());
            curriculumSubject.setModifiedBy(user.getId());
            curriculumSubjectRepository.save(curriculumSubject);
        }
        subject.setStatus(Constants.STATUS_TYPE.DELETED);
        subject.setModifiedAt(LocalDateTime.now());
        subject.setModifiedBy(user.getId());

        return subjectRepository.save(subject);
    }
}
