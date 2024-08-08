package vn.attendance.service.teacherSubject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Subject;
import vn.attendance.model.TeacherSubject;
import vn.attendance.model.Users;
import vn.attendance.repository.SubjectRepository;
import vn.attendance.repository.TeacherRepository;
import vn.attendance.repository.TeacherSubjectRepository;
import vn.attendance.service.teacher.request.AddTeacherSubjectRequest;
import vn.attendance.service.teacherSubject.response.TeacherSubjectDto;
import vn.attendance.service.teacherSubject.service.TeacherSubjectService;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeacherSubjectImpl implements TeacherSubjectService {

    @Autowired
    TeacherSubjectRepository teacherSubjectRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    SubjectRepository subjectRepository;


    @Override
    public List<TeacherSubjectDto> findAllTeacherSubject(Integer teacherId) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }
        return teacherSubjectRepository.findSubjectsByTeacher(teacherId);
    }

    @Override
    public AddTeacherSubjectRequest addTeacherSubject(AddTeacherSubjectRequest request, Integer option) throws AmsException {
        Users users = BaseUserDetailsService.USER.get();
        if (users == null) throw new AmsException(MessageCode.USER_NOT_FOUND);

        Users teacher = teacherRepository.findTeacherByCode(request.getTeacherCode());
        if (teacher == null) {
            if(option == 1){
                throw new AmsException(MessageCode.TEACHER_NOT_FOUND);
            }
            request.setErrorMess(MessageCode.TEACHER_NOT_FOUND.getCode());
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            return request;
        }
        Subject subject = subjectRepository.findSubjectByCode(request.getSubjectCode());
        if (subject == null) {
            if(option == 1){
                throw new AmsException(MessageCode.SUBJECT_NOT_FOUND);
            }
            request.setErrorMess(MessageCode.SUBJECT_NOT_FOUND.getCode());
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            return request;
        }
        TeacherSubject old = teacherSubjectRepository.findByTeacherAndSubId(teacher.getId(),subject.getId());
        if (old != null) {
            if(old.getStatus().equals(Constants.STATUS_TYPE.DELETED)) {
                old.setStatus(Constants.STATUS_TYPE.ACTIVE);
                old.setModifiedBy(users.getId());
                old.setModifiedAt(LocalDateTime.now());
                teacherSubjectRepository.save(old);
                request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
                return request;
            }
            if(option == 1){
                throw new AmsException(MessageCode.SUBJECT_ALREADY_ASSIGNED_TO_TEACHER);
            }
            request.setErrorMess(MessageCode.SUBJECT_ALREADY_ASSIGNED_TO_TEACHER.getCode());
            request.setStatus(Constants.REQUEST_STATUS.FAILED);
            return request;
        }
        TeacherSubject teacherSubject = new TeacherSubject();
        teacherSubject.setSubjectId(subject.getId());
        teacherSubject.setTeacherId(teacher.getId());
        teacherSubject.setStatus(Constants.STATUS_TYPE.ACTIVE);
        teacherSubject.setCreatedAt(LocalDateTime.now());
        teacherSubject.setCreatedBy(users.getId());
        teacherSubjectRepository.save(teacherSubject);

        request.setStatus(Constants.REQUEST_STATUS.SUCCESS);
        return request;
    }

    @Override
    public List<AddTeacherSubjectRequest> importTeacherSubject(List<AddTeacherSubjectRequest> requests) throws AmsException {
        for (AddTeacherSubjectRequest request : requests
        ){
            addTeacherSubject(request, 0);
        }
        return requests;
    }
}
