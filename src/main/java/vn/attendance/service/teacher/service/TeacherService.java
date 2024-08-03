package vn.attendance.service.teacher.service;

import org.springframework.data.domain.Page;
import vn.attendance.exception.AmsException;
import vn.attendance.model.Subject;
import vn.attendance.model.TeacherSubject;
import vn.attendance.model.Users;
import vn.attendance.service.subject.request.EditSubjectRequest;
import vn.attendance.service.teacher.request.EditTeacherSubjectRequest;
import vn.attendance.service.teacher.response.ITeacherDto;
import vn.attendance.service.teacher.response.TeacherDto;
import vn.attendance.service.user.service.response.UsersDto;

import java.util.List;
import java.util.Optional;

public interface TeacherService {

    Page<UsersDto> findTeacher(String search, String status, Integer gender, int page, int size);

    List<TeacherDto> teacherByClassRoom(Integer id);

    List<TeacherDto> teacherBySubject(Integer id);
    TeacherSubject deleteTeacherSubject(Integer id) throws AmsException;
    TeacherSubject updateTeacherSubject(Integer id, EditTeacherSubjectRequest editSubjectRequest) throws AmsException;


    List<ITeacherDto> getTeacherDropList(Integer subjectId);
}
