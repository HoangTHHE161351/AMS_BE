package vn.attendance.service.teacherSubject.service;

import vn.attendance.exception.AmsException;
import vn.attendance.service.teacher.request.AddTeacherSubjectRequest;
import vn.attendance.service.teacherSubject.response.TeacherSubjectDto;

import java.util.List;

public interface TeacherSubjectService {

    List<TeacherSubjectDto> findAllTeacherSubject(Integer teacherId) throws AmsException;

    AddTeacherSubjectRequest addTeacherSubject(AddTeacherSubjectRequest request, Integer option) throws AmsException;

    List<AddTeacherSubjectRequest> importTeacherSubject(List<AddTeacherSubjectRequest> requests) throws AmsException;
}
