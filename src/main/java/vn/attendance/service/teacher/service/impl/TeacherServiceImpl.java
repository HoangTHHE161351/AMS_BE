package vn.attendance.service.teacher.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.attendance.config.authen.BaseUserDetailsService;
import vn.attendance.exception.AmsException;
import vn.attendance.model.TeacherSubject;
import vn.attendance.model.Users;
import vn.attendance.repository.TeacherRepository;
import vn.attendance.repository.TeacherSubjectRepository;
import vn.attendance.repository.UsersRepository;
import vn.attendance.service.teacher.request.EditTeacherSubjectRequest;
import vn.attendance.service.teacher.response.ITeacherDto;
import vn.attendance.service.teacher.response.TeacherDto;
import vn.attendance.service.teacher.service.TeacherService;
import vn.attendance.service.user.service.response.UsersDto;
import vn.attendance.util.Constants;
import vn.attendance.util.MessageCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    private TeacherSubjectRepository teacherSubjectRepository;

    @Override
    public Page<UsersDto> findTeacher(String search, String status, Integer gender, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return teacherRepository.findTeacher(search, status, gender, pageable);
    }

    @Override
    public List<TeacherDto> teacherByClassRoom(Integer id) {
        return teacherRepository.teacherByClassRoom(id);
    }

    @Override
    public List<TeacherDto> teacherBySubject(Integer id) {

        return teacherRepository.teacherBySubject(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeacherSubject deleteTeacherSubject(Integer id) throws AmsException{
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        TeacherSubject teacherSubject = teacherSubjectRepository.findById(id).orElse(null);
        if(teacherSubject == null || teacherSubject.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw new AmsException(MessageCode.TEACHER_SUBJECT_NOT_FOUND);
        }

        teacherSubject.setStatus(Constants.STATUS_TYPE.DELETED);
        teacherSubject.setModifiedAt(LocalDateTime.now());
        teacherSubject.setModifiedBy(user.getId());
        return teacherSubjectRepository.save(teacherSubject);
    }

    @Override
    public TeacherSubject updateTeacherSubject(Integer id, EditTeacherSubjectRequest editTeacherSubjectRequest) throws AmsException {
        Users user = BaseUserDetailsService.USER.get();
        if (user == null) {
            throw new AmsException(MessageCode.USER_NOT_FOUND);
        }

        TeacherSubject teacherSubject = teacherSubjectRepository.findById(id).orElse(null);
        if(teacherSubject == null || teacherSubject.getStatus().equals(Constants.STATUS_TYPE.DELETED)){
            throw new AmsException(MessageCode.TEACHER_SUBJECT_NOT_FOUND);
        }

        teacherSubject.setSubjectId(editTeacherSubjectRequest.getSubjectId());
        teacherSubject.setTeacherId(editTeacherSubjectRequest.getTeacherId());
        teacherSubject.setModifiedAt(LocalDateTime.now());
        teacherSubject.setModifiedBy(BaseUserDetailsService.USER.get().getId());
        teacherSubject.setModifiedBy(user.getId());
        return teacherSubjectRepository.save(teacherSubject);
    }

    @Override
    public List<ITeacherDto> getTeacherDropList(Integer subjectId) {
        return teacherRepository.getTeacherDropList(subjectId);
    }


}
